package com.industry.item;

import com.ibm.icu.util.TimeUnitAmount;
import com.industry.MaterialsAndTools.LifeStealSword;
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
import org.apache.logging.log4j.message.ExitMessage;

public class ModItems {

    public static final Item LIFE_STEAL_SWORD = registerItem("life_steal_sword", new LifeStealSword(new Item.Settings()));

    public static final Item CHISEL = registerItem("shaper", new ChangerItem(new Item.Settings()));
    public static final Item SWITCHER = registerItem("mob_switcher", new MobSwitcherItem(new Item.Settings()));
    public static final Item CARVER = registerItem("carver", new CarverItem(new Item.Settings()));
    public static final Item LAUNCHER = registerItem("launcher", new Launcher(new Item.Settings()));
    public static final Item WOOL_WAND = registerItem("wand_of_wools", new WoolWand(new Item.Settings()));
    public static final Item SPECTATOR = registerItem("spectator", new Spectator(new Item.Settings()));

    public static final Item RAILGUN = registerItem("railgun", new Railgun(new Item.Settings()));
    public static final Item ORBITAL_LAZER_CANNON = registerItem("orbital_laser_cannon", new OrbitalLazerCannon(new Item.Settings()));
    public static final Item GRAVITON_LAUNCHER = registerItem("graviton_launcher", new GravitonLauncher(new Item.Settings()));

    public static final Item REGENERATION_AMULET = registerItem("regeneration_amulet", new RegenerationAmulet(new Item.Settings()));
    public static final Item RESISTANCE_AMULET = registerItem("resistance_amulet", new ResistanceAmulet(new Item.Settings()));
    public static final Item SPEED_AMULET = registerItem("speed_amulet", new SpeedAmulet(new Item.Settings()));
    public static final Item STRENGTH_AMULET = registerItem("strength_amulet", new StrengthAmulet(new Item.Settings()));
    public static final Item WorldEditAxe =  registerItem("world_edit_axe", new WorldEditAxe(new Item.Settings()));


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
            entries.add(LAUNCHER);
            entries.add(WOOL_WAND);
            entries.add(SPECTATOR);
            entries.add(WorldEditAxe);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(RAILGUN);
            entries.add(ORBITAL_LAZER_CANNON);
            entries.add(GRAVITON_LAUNCHER);
            entries.add(LIFE_STEAL_SWORD);
        });
    }

}
