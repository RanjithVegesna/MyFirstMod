package com.industry.Blocks;

import com.industry.Mod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block JUMP_PAD = registerBlock("jump_pad",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(1)));

    public static Block JUMP_PAD_15 = registerBlock("jump_pad_1.5",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(1.5F)));

    public static Block JUMP_PAD_2 = registerBlock("jump_pad_2",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(2)));

    public static Block JUMP_PAD_3 = registerBlock("jump_pad_3",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(3)));

    public static Block JUMP_PAD_4 = registerBlock("jump_pad_4",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(4)));

    public static Block JUMP_PAD_5 = registerBlock("jump_pad_5",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(5)));

    public static Block JUMP_PAD_6 = registerBlock("jump_pad_6",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(6)));

    public static Block JUMP_PAD_7 = registerBlock("jump_pad_7",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(7)));

    public static Block JUMP_PAD_8 = registerBlock("jump_pad_8",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(8)));

    public static Block JUMP_PAD_9 = registerBlock("jump_pad_9",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(9)));

    public static Block JUMP_PAD_10 = registerBlock("jump_pad_10",
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(10)));

    public static Block registerBlock(String name, Block block) {
        registerBlockItems(name,  block);
        return Registry.register(Registries.BLOCK, Identifier.of(Mod.MOD_ID,  name), block);
    }

    public static void registerBlockItems(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Mod.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(JUMP_PAD);
            entries.add(JUMP_PAD_15);
            entries.add(JUMP_PAD_2);
            entries.add(JUMP_PAD_3);
            entries.add(JUMP_PAD_4);
            entries.add(JUMP_PAD_5);
            entries.add(JUMP_PAD_6);
            entries.add(JUMP_PAD_7);
            entries.add(JUMP_PAD_8);
            entries.add(JUMP_PAD_9);
            entries.add(JUMP_PAD_10);
        });


    }
}
