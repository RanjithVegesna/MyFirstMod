package com.industry.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JumpPad extends Block {

    public JumpPad(Settings settings, float jumpVelocityMultiplier) {
        super(settings.jumpVelocityMultiplier(jumpVelocityMultiplier));
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {

        entity.fallDistance = 0;
        super.onLandedUpon(world, state, pos, entity, fallDistance);
    }
}
