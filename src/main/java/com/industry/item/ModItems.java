package com.industry.item;

import com.industry.item.amulet.RegenerationAmulet;
import com.industry.item.amulet.ResistanceAmulet;
import com.industry.item.amulet.SpeedAmulet;
import com.industry.item.amulet.StrengthAmulet;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static Item CHISEL = registerItem("shaper", ChangerItem.Chisel);
    public static Item SWITCHER = registerItem("mob_switcher", MobSwitcherItem.Chisel);
    public static Item CARVER =  registerItem("carver", CarverItem.Chisel);

    public static Item REGENERATION_AMULET = registerItem("regeneration_amulet", RegenerationAmulet.REGENERATION_AMULET);
    public static Item RESISTANCE_AMULET = registerItem("resistance_amulet", ResistanceAmulet.RESISTANCE_AMULET);
    public static Item SPEED_AMULET = registerItem("speed_amulet", SpeedAmulet.SPEED_AMULET);
    public static Item STRENGTH_AMULET = registerItem("strength_amulet", StrengthAmulet.STRENGTH_AMULET);



    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of("industry", name), item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(REGENERATION_AMULET);
            entries.add(RESISTANCE_AMULET);
            entries.add(SPEED_AMULET);
            entries.add(STRENGTH_AMULET);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(CHISEL);
            entries.add(SWITCHER);
            entries.add(CARVER);
        });
    }

}
