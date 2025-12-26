package com.industry.Blocks;

import com.industry.Mod;
import com.industry.Blocks.BeaconBeamTrapBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static Block BEACON_BEAM_TRAP = registerBlock("beacon_beam_trap",
            new BeaconBeamTrapBlock(AbstractBlock.Settings.create().hardness(4f).sounds(BlockSoundGroup.GLASS).slipperiness(1)));

    public static Block JUMP_PAD = registerBlock("jump_pad",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 1));

    public static Block JUMP_PAD_15 = registerBlock("jump_pad_1.5",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 1.5f));

    public static Block JUMP_PAD_2 = registerBlock("jump_pad_2",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 2));

    public static Block JUMP_PAD_3 = registerBlock("jump_pad_3",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 3));

    public static Block JUMP_PAD_4 = registerBlock("jump_pad_4",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 4));

    public static Block JUMP_PAD_5 = registerBlock("jump_pad_5",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 5));

    public static Block JUMP_PAD_6 = registerBlock("jump_pad_6",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 6));

    public static Block JUMP_PAD_7 = registerBlock("jump_pad_7",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 7));

    public static Block JUMP_PAD_8 = registerBlock("jump_pad_8",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 8));

    public static Block JUMP_PAD_9 = registerBlock("jump_pad_9",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 9));

    public static Block JUMP_PAD_10 = registerBlock("jump_pad_10",
            new JumpPad(AbstractBlock.Settings.create().hardness(4f).requiresTool().sounds(BlockSoundGroup.CALCITE).slipperiness(1), 10));

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
