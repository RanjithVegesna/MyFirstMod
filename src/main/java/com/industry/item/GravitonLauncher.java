package com.industry.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GravitonLauncher extends Item {

    public int delay = 20;
    public int useTime = 200;
    public long timeWhenHit;
    public BlockPos hitPos;

    public GravitonLauncher(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Vec3d playerPos = user.getPos();
            Vec3d lookingVec = user.getRotationVec(1);
            BlockPos hitPosition = BlockPos.ofFloored(playerPos.add(lookingVec.multiply(5)));

            if (world.getBlockState(hitPos).isAir()) {
                timeWhenHit = world.getTime();
                hitPos = hitPosition;
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (entity instanceof PlayerEntity player) {
                if (timeWhenHit + delay < timeWhenHit < timeWhenHit + delay + useTime)
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
