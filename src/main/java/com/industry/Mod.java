package com.industry;

import com.industry.Blocks.ModBlocks;
import com.industry.item.ModItems;
import com.industry.item.amulet.RegenerationAmulet;
import com.industry.item.amulet.ResistanceAmulet;
import com.industry.item.amulet.SpeedAmulet;
import com.industry.item.amulet.StrengthAmulet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "industry";

	@Override
	public void onInitialize() {
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();


        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                RegenerationAmulet.implementAmulet(player);
                ResistanceAmulet.implementAmulet(player);
                SpeedAmulet.implementAmulet(player);
                StrengthAmulet.implementAmulet(player);
            }
        });
	}
}