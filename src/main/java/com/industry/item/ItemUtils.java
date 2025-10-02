package com.industry.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class ItemUtils {
    public static boolean isInInventory(PlayerEntity player, Item item) {
        for (ItemStack stack : player.getInventory().main){
            if (stack.getItem() == item){
                return true;
            }
        }
        return false;
    }

    public static boolean isInEitherHand(PlayerEntity player,  Item item) {
        return player.getMainHandStack().getItem() == item || player.getOffHandStack().getItem() == item;
    }

    public static void spawnParticles(World world, LivingEntity entity, ParticleEffect particleType, int particleCount) {
        double centerX = entity.getX();
        double centerY = entity.getY() + 1; // slightly above head
        double centerZ = entity.getZ();

        for (int i = 0; i < particleCount; i++) {

            double offsetX = (world.random.nextDouble() - 0.5) * 1.5;
            double offsetY = world.random.nextDouble();
            double offsetZ = (world.random.nextDouble() - 0.5) * 1.5;


            double velocityX = (world.random.nextDouble() - 0.5) * 0.1;
            double velocityY = (world.random.nextDouble()) * 0.1;
            double velocityZ = (world.random.nextDouble() - 0.5) * 0.1;


            world.addParticle(particleType,
                    centerX + offsetX,
                    centerY + offsetY,
                    centerZ + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ);
        }
    }
}
