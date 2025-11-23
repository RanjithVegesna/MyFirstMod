package com.industry.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WoolWand extends Item {

    Map<LivingEntity, Long> WoolEntities = new HashMap<>();

    public Random random = new Random();


    public WoolWand(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ArrayList<Block> items = new ArrayList<>();
        items.add(Blocks.WHITE_WOOL);
        items.add(Blocks.ORANGE_WOOL);
        items.add(Blocks.MAGENTA_WOOL);
        items.add(Blocks.LIGHT_BLUE_WOOL);
        items.add(Blocks.YELLOW_WOOL);
        items.add(Blocks.LIME_WOOL);
        items.add(Blocks.PINK_WOOL);
        items.add(Blocks.GRAY_WOOL);
        items.add(Blocks.LIGHT_GRAY_WOOL);
        items.add(Blocks.CYAN_WOOL);
        items.add(Blocks.PURPLE_WOOL);
        items.add(Blocks.BLUE_WOOL);
        items.add(Blocks.BROWN_WOOL);
        items.add(Blocks.GREEN_WOOL);
        items.add(Blocks.RED_WOOL);
        items.add(Blocks.BLACK_WOOL);

        BlockPos posBelowPlayer = user.getBlockPos().subtract(new Vec3i(0, 1, 0));
        ArrayList<BlockPos> blockPoses = new ArrayList<>();
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, 1)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, 0)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, -1)));

        blockPoses.add(posBelowPlayer.add(new Vec3i(0, 0, 1)));
        blockPoses.add(posBelowPlayer);
        blockPoses.add(posBelowPlayer.add(new Vec3i(0, 0, -1)));

        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, 1)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, 0)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, -1)));


        if (user.isSneaking()) {
            WoolEntities.put(user, world.getTime());
            return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
        }

        for (BlockPos blockPos : blockPoses) {

            Block wool = items.get(random.nextInt(items.size()));
            world.setBlockState(blockPos, wool.getDefaultState());
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) {return;}
        if (!WoolEntities.containsKey(player)) {return;}


        ArrayList<Block> items = new ArrayList<>();
        items.add(Blocks.WHITE_WOOL);
        items.add(Blocks.ORANGE_WOOL);
        items.add(Blocks.MAGENTA_WOOL);
        items.add(Blocks.LIGHT_BLUE_WOOL);
        items.add(Blocks.YELLOW_WOOL);
        items.add(Blocks.LIME_WOOL);
        items.add(Blocks.PINK_WOOL);
        items.add(Blocks.GRAY_WOOL);
        items.add(Blocks.LIGHT_GRAY_WOOL);
        items.add(Blocks.CYAN_WOOL);
        items.add(Blocks.PURPLE_WOOL);
        items.add(Blocks.BLUE_WOOL);
        items.add(Blocks.BROWN_WOOL);
        items.add(Blocks.GREEN_WOOL);
        items.add(Blocks.RED_WOOL);
        items.add(Blocks.BLACK_WOOL);

        BlockPos posBelowPlayer = player.getBlockPos().subtract(new Vec3i(0, 1, 0));
        ArrayList<BlockPos> blockPoses = new ArrayList<>();
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, 1)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, 0)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(1, 0, -1)));

        blockPoses.add(posBelowPlayer.add(new Vec3i(0, 0, 1)));
        blockPoses.add(posBelowPlayer);
        blockPoses.add(posBelowPlayer.add(new Vec3i(0, 0, -1)));

        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, 1)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, 0)));
        blockPoses.add(posBelowPlayer.add(new Vec3i(-1, 0, -1)));

        for (BlockPos blockPos : blockPoses) {

            Block wool = items.get(random.nextInt(items.size()));
            boolean flag = false;
            for (Block block : items) {
                if (block.getDefaultState() == world.getBlockState(blockPos)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) world.setBlockState(blockPos, wool.getDefaultState());
        }
        Long startTime = WoolEntities.get(player);
        if (startTime != null && world.getTime() > startTime + 400) {
            WoolEntities.remove(player);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
