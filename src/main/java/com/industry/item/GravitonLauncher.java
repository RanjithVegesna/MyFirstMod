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

import java.util.List;

public class GravitonLauncher extends Item {

    public int delay = 20;
    public int useTime = 200;
    public long timeWhenHit;
    public BlockPos hitPos;
    public Box areaOfEffectBox;

    public GravitonLauncher(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d playerPos = user.getPos();
            Vec3d lookingVec = user.getRotationVec(1);
            BlockPos hitPosition = BlockPos.ofFloored(playerPos.add(lookingVec.multiply(5)));

            if (world.getBlockState(hitPosition).isAir()) {
                timeWhenHit = world.getTime();
                hitPos = hitPosition;
                areaOfEffectBox = null;
                GravitonLauncherPayload payload = new GravitonLauncherPayload(hitPosition.toCenterPos());
                if (user instanceof ServerPlayerEntity serverPlayer) {
                    ServerPlayNetworking.send(serverPlayer, payload);
                }
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            long currentTime = world.getTime();
            if (timeWhenHit + delay < currentTime && currentTime < timeWhenHit + delay + useTime) {

                // Create the area of effect box if not already
                if (areaOfEffectBox == null && hitPos != null) {
                    int size = 10;
                    Vec3d pos1 = new Vec3d(hitPos.getX() + size, hitPos.getY() + size, hitPos.getZ() + size);
                    Vec3d pos2 = new Vec3d(hitPos.getX() - size, hitPos.getY() - size, hitPos.getZ() - size);
                    areaOfEffectBox = new Box(pos2, pos1);
                    Mod.LOGGER.info("Area of effect box created: {} - {}", pos2, pos1);
                }

                if (areaOfEffectBox != null) {
                    List<Entity> Entities = world.getEntitiesByClass(Entity.class, areaOfEffectBox, e -> true);
                    for (Entity Entity : Entities) {
                        Vec3d effectPos = Vec3d.ofCenter(hitPos);
                        Vec3d delta = effectPos.subtract(Entity.getPos());
                        double distance = delta.length();
                        if (distance > 0.1) {
                            // Pull strength, tweak 0.2 to adjust effect
                            Vec3d acceleration = delta.normalize().multiply(0.5 * Math.min(distance, 10));
                            Entity.addVelocity(acceleration.x, acceleration.y, acceleration.z);
                            Entity.velocityModified = true; // Important for Minecraft to register velocity
                        }
                    }
                }
            }

            // Clear the box after effect ends
            if (currentTime >= timeWhenHit + delay + useTime) {
                areaOfEffectBox = null;
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
