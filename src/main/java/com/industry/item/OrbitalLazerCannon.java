package com.industry.item;

import classes.Table;
import com.industry.Mod;
import com.industry.packets.OrbitalLazerCannonPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class OrbitalLazerCannon extends Item {

    public static final int DELAY = 120;
    public static Table playerTable;

    public OrbitalLazerCannon(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (playerTable == null) {
            playerTable = new Table(new ArrayList<Class<?>>(List.of(PlayerEntity.class, BlockPos.class, Integer.class)));
            playerTable.nameColumns(new ArrayList<>(List.of("player", "hitPos", "time")));
        }
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) return TypedActionResult.pass(stack);

        // Start and end for raycast
        Vec3d eyePos = user.getPos().add(0, user.getStandingEyeHeight(), 0);
        Vec3d direction = user.getRotationVec(1.0F);
        Vec3d end = eyePos.add(direction.multiply(500));

        RaycastContext context = new RaycastContext(
                eyePos, end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                user
        );

        BlockHitResult result = world.raycast(context);

        if (result.getType() == BlockHitResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();

            Mod.LOGGER.info("Orbital Target: {}", pos);

            playerTable.add(user, pos, world.getTime());

            // Send packet to client to draw warning / beam animation
            if (user instanceof ServerPlayerEntity serverPlayer) {
                ServerPlayNetworking.send(
                        serverPlayer,
                        new OrbitalLazerCannonPayload(pos.toCenterPos())
                );
            }
        }

        return TypedActionResult.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        for (PlayerEntity player : world.getPlayers()) {

            Integer lastTime = (Integer) playerTable.query("time", "player", player);
            BlockPos pos = (BlockPos) playerTable.query("hitPos", "player", player);
            // Gun was never fired
            if (lastTime == null) continue;

            long currentTime = world.getTime();

            if (currentTime < lastTime + DELAY) continue; // wait until 6 seconds have passed

            // NUKE RAIN (your vertical explosion chain)
            for (int i = 319; i > -64; i -= 10) {
                world.createExplosion(
                        null,
                        pos.getX(),
                        i,
                        pos.getZ(),
                        35,
                        World.ExplosionSourceType.TNT
                );
            }

            // Clear stored data so it doesn't fire again
            playerTable.delete(player, "player");
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
