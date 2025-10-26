package com.industry.item;

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
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext;

public class OrbitalLazerCannon extends Item {

    public long lastTimeUsed = 0;
    public int delay = 120;
    public BlockPos blockPos;

    public OrbitalLazerCannon(Settings settings) {
        super(settings);
    }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

            Vec3d start = user.getPos().add(0, user.getStandingEyeHeight(), 0); // player eyes
            Vec3d direction = user.getRotationVec(1.0F); // normalized look direction
            Vec3d end = start.add(direction.multiply(500)); // 500 blocks forward

            RaycastContext context = new RaycastContext(
                    start,
                    end,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    user
            );

            BlockHitResult hitResult = world.raycast(context);

            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos targetPos = hitResult.getBlockPos();
                Mod.LOGGER.info("Target Vector at ({}, {}, {})", targetPos.getX(), targetPos.getY(), targetPos.getZ());
                OrbitalLazerCannonPayload payload = new OrbitalLazerCannonPayload(targetPos.toCenterPos());
                if (user instanceof ServerPlayerEntity serverPlayer) {
                    ServerPlayNetworking.send(serverPlayer, payload);
                }
                blockPos = targetPos;

                lastTimeUsed = world.getTime();
            } else {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        @Override
        public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
            if (world.isClient) return;
            if (blockPos != null && world.getTime() >= lastTimeUsed + delay) {
                for (int i = 319; i > -64; i -= 10) {
                    world.createExplosion(null, blockPos.getX(), i, blockPos.getZ(), 35, World.ExplosionSourceType.TNT);
                }
                blockPos = null; // reset so it doesn’t keep exploding
            }
            super.inventoryTick(stack, world, entity, slot, selected);
        }

}
