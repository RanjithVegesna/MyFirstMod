package com.industry.item;

import com.industry.Mod;
import com.industry.packets.GravitonLauncherPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Improved Graviton Launcher:
 * - per-player state (no shared fields)
 * - configurable delay/duration/radius/strength
 * - smooth distance falloff and velocity clamping
 * - cheap tick skipping + processing cap to avoid server lag
 */
public class GravitonLauncher extends Item {

    // Configurable constants
    private static final int DELAY_TICKS = 20;         // time before effect starts
    private static final int DURATION_TICKS = 200;     // how long the pull lasts
    private static final int UPDATE_INTERVAL = 2;      // process every N ticks (less work)
    private static final int RADIUS = 10;              // effect radius
    private static final double MAX_PULL = 0.6;        // base pull multiplier
    private static final double MAX_ENTITY_SPEED = 3.0;// clamp entity speed
    private static final int MAX_ENTITIES_PER_UPDATE = 128; // cap per update to protect server

    // Per-player active state. Keyed by player UUID so multiple players can use independently.
    private static final Map<UUID, GravitonState> STATES = new ConcurrentHashMap<>();

    public GravitonLauncher(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

        Vec3d playerPos = user.getPos();
        Vec3d lookVec = user.getRotationVec(1.0F);
        // target 5 blocks ahead (you can use a raycast for more precision)
        BlockPos target = BlockPos.ofFloored(playerPos.add(lookVec.multiply(5.0)));

        // store per-player state with world time
        GravitonState s = new GravitonState(target, world.getTime());
        STATES.put(user.getUuid(), s);

        // send client payload to render warning/particle
        if (user instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, new GravitonLauncherPayload(target.toCenterPos()));
        }

        Mod.LOGGER.info("GravitonLauncher: queued pull for {} at {} (start in {} ticks)", user.getName().getString(), target, DELAY_TICKS);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // Only run on server and only for players
        if (world.isClient || !(entity instanceof PlayerEntity player)) {
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }

        GravitonState state = STATES.get(player.getUuid());
        if (state == null) {
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }

        long now = world.getTime();

        // If effect not started yet: wait for delay
        if (now < state.startTime + DELAY_TICKS) {
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }

        // If effect finished: cleanup
        if (now >= state.startTime + DELAY_TICKS + DURATION_TICKS) {
            STATES.remove(player.getUuid());
            Mod.LOGGER.info("GravitonLauncher: finished for player {}", player.getName().getString());
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }

        // Tick-skip updates to reduce per-tick load
        if ((now - state.lastProcessedTick) < UPDATE_INTERVAL) {
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }
        state.lastProcessedTick = now;

        // Build effect box if needed
        if (state.box == null) {
            Vec3d c1 = new Vec3d(state.hitPos.getX() - RADIUS, state.hitPos.getY() - RADIUS, state.hitPos.getZ() - RADIUS);
            Vec3d c2 = new Vec3d(state.hitPos.getX() + RADIUS, state.hitPos.getY() + RADIUS, state.hitPos.getZ() + RADIUS);
            state.box = new Box(c1, c2);
        }

        // Performance safety: skip if chunk is not loaded around center
        if (!world.isChunkLoaded(state.hitPos.getX() >> 4, state.hitPos.getZ() >> 4)) {
            super.inventoryTick(stack, world, entity, slot, selected);
            return;
        }

        // Pull entities (cap processed entities to avoid spikes)
        int processed = 0;
        for (Entity e : world.getEntitiesByClass(Entity.class, state.box, ent -> ent != player)) {
            if (processed++ > MAX_ENTITIES_PER_UPDATE) break;

            Vec3d center = Vec3d.ofCenter(state.hitPos);
            Vec3d delta = center.subtract(e.getPos());
            double distance = delta.length();

            if (distance < 0.0001 || distance > RADIUS) continue;

            // falloff: stronger when closer; normalized falloff in [0,1]
            double falloff = 1.0 - (distance / RADIUS);
            double pullStrength = MAX_PULL * falloff;

            Vec3d acceleration = delta.normalize().multiply(pullStrength);

            // apply acceleration to velocity and clamp final speed
            Vec3d currentVel = e.getVelocity();
            Vec3d newVel = currentVel.add(acceleration);

            double speed = newVel.length();
            if (speed > MAX_ENTITY_SPEED) {
                newVel = newVel.normalize().multiply(MAX_ENTITY_SPEED);
            }

            // Very small vertical clamping to avoid huge upward launches
            double maxVertical = 1.2;
            if (Math.abs(newVel.y) > maxVertical) {
                newVel = new Vec3d(newVel.x, Math.signum(newVel.y) * maxVertical, newVel.z);
            }

            // Prevent moving entity into a solid block directly: sample new position and reduce if colliding
            Vec3d projected = e.getPos().add(newVel);
            BlockPos projectedPos = BlockPos.ofFloored(projected);
            if (world.getBlockState(projectedPos).isSolidBlock(world, projectedPos)) {
                // reduce velocity and skip pushing through solid blocks
                newVel = new Vec3d(newVel.x * 0.2, Math.min(newVel.y, 0.2), newVel.z * 0.2);
            }

            e.setVelocity(newVel);
            e.velocityModified = true;
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    // Per-player state holder
    private static class GravitonState {
        final BlockPos hitPos;
        final long startTime;
        Box box;
        long lastProcessedTick = -1;

        GravitonState(BlockPos hitPos, long startTime) {
            this.hitPos = hitPos;
            this.startTime = startTime;
        }
    }
}