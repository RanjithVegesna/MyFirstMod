package com.industry.MaterialsAndTools;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static com.industry.MaterialsAndTools.ModMaterials.LifeSteal;

public class LifeStealPickaxe extends PickaxeItem {
    public LifeStealPickaxe( Settings settings) {
        super(LifeSteal, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (world.isClient) {return false;}
        if (miner.isSneaking()) return super.postMine(stack, world, state, pos, miner);

        List<BlockPos> blockPosesThatNeedToBeMined = new ArrayList<>();

        for (int i = -1; i <= 1 ; i++) {
            for (int j = -1; j <= 1 ; j++) {
                for (int k = -1; k <= 1 ; k++) {
                    blockPosesThatNeedToBeMined.add(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k));
                }
            }
        }

        for (BlockPos blockPos : blockPosesThatNeedToBeMined) {
            world.breakBlock(blockPos, true);
        }
        return true;
    }
}
