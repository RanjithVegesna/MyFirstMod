package com.industry.item;

import com.industry.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.industry.item.ItemUtils.createBlockCycle;

public class CarverItem extends Item {
    public static final Map<Block, Block> BLOCKS = new HashMap<>() {{
        putAll(createBlockCycle(
                Blocks.COBBLESTONE,
                Blocks.COBBLESTONE_STAIRS,
                Blocks.COBBLESTONE_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.STONE,
                Blocks.STONE_STAIRS,
                Blocks.STONE_SLAB,
                Blocks.STONE_PRESSURE_PLATE
        ));

        putAll(createBlockCycle(
                Blocks.STONE_BRICKS,
                Blocks.STONE_BRICK_STAIRS,
                Blocks.STONE_BRICK_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.RED_SANDSTONE,
                Blocks.RED_SANDSTONE_STAIRS,
                Blocks.RED_SANDSTONE_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.SANDSTONE,
                Blocks.SANDSTONE_STAIRS,
                Blocks.SANDSTONE_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.QUARTZ_BLOCK,
                Blocks.QUARTZ_STAIRS,
                Blocks.QUARTZ_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.NETHER_BRICKS,
                Blocks.NETHER_BRICK_STAIRS,
                Blocks.NETHER_BRICK_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.PURPUR_BLOCK,
                Blocks.PURPUR_STAIRS,
                Blocks.PURPUR_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.BLACKSTONE,
                Blocks.BLACKSTONE_STAIRS,
                Blocks.BLACKSTONE_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE_BRICKS,
                Blocks.BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,
                Blocks.BLACKSTONE_SLAB,
                Blocks.POLISHED_BLACKSTONE_SLAB,
                Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,
                Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE
        ));


        putAll(createBlockCycle(
                Blocks.POLISHED_BLACKSTONE_BRICKS,
                Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,
                Blocks.POLISHED_BLACKSTONE_BRICK_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.COBBLED_DEEPSLATE,
                Blocks.COBBLED_DEEPSLATE_STAIRS,
                Blocks.COBBLED_DEEPSLATE_SLAB
        ));

        putAll(createBlockCycle(
                Blocks.DEEPSLATE_TILES,
                Blocks.DEEPSLATE_TILE_STAIRS,
                Blocks.DEEPSLATE_TILE_SLAB
                // No pressure plate or button for deepslate tiles
        ));

        putAll(createBlockCycle(
                Blocks.DEEPSLATE_BRICKS,
                Blocks.DEEPSLATE_BRICK_STAIRS,
                Blocks.DEEPSLATE_BRICK_SLAB
                // No pressure plate or button for deepslate bricks
        ));

        putAll(createBlockCycle(
                Blocks.POLISHED_DEEPSLATE,
                Blocks.POLISHED_DEEPSLATE_STAIRS,
                Blocks.POLISHED_DEEPSLATE_SLAB
                // No pressure plate or button for polished deepslate
        ));

        putAll(createBlockCycle(
                Blocks.BASALT,
                Blocks.SMOOTH_BASALT,
                Blocks.POLISHED_BASALT

        ));
    }};


    public CarverItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (BLOCKS.containsKey(clickedBlock)) {
            if (!world.isClient) {
                world.setBlockState(context.getBlockPos(), BLOCKS.get(clickedBlock).getDefaultState());
                Mod.LOGGER.info("Used Chisel for " + clickedBlock);
                world.playSound(null, context.getBlockPos(),  SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        return super.useOnBlock(context);
    }
}
