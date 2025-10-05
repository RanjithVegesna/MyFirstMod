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

public class ChangerItem extends Item {

    public static Item Chisel = new ChangerItem(new Item.Settings());

    public static final Map<Block, Block> BLOCKS = new HashMap<>() {{
        putAll(createBlockCycle(
                Blocks.COBBLESTONE,
                Blocks.STONE,
                Blocks.STONE_BRICKS,
                Blocks.CRACKED_STONE_BRICKS
        ));
        putAll(createBlockCycle(
                Blocks.RED_SANDSTONE,
                Blocks.SMOOTH_RED_SANDSTONE,
                Blocks.CHISELED_RED_SANDSTONE
        ));
        putAll(createBlockCycle(
                Blocks.SANDSTONE,
                Blocks.SMOOTH_SANDSTONE,
                Blocks.CHISELED_SANDSTONE
        ));
    }};


    public ChangerItem(Settings settings) {
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
