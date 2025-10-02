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
            new Block(AbstractBlock.Settings.create().hardness(4f).breakInstantly().sounds(BlockSoundGroup.CALCITE).jumpVelocityMultiplier(2)));

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
        });


    }
}
