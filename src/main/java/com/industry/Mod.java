package com.industry;

import com.industry.Blocks.ModBlocks;
import com.industry.item.Launcher;
import com.industry.item.ModItems;
import com.industry.item.amulet.RegenerationAmulet;
import com.industry.item.amulet.ResistanceAmulet;
import com.industry.item.amulet.SpeedAmulet;
import com.industry.item.amulet.StrengthAmulet;
import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "industry";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean hit = false;

	@Override
	public void onInitialize() {

        LOGGER.info("Initializing Items of" + MOD_ID);
        ModItems.registerModItems();
        LOGGER.info("Registered Items of" + MOD_ID);
        LOGGER.info("Initializing Blocks of" + MOD_ID);
        ModBlocks.registerModBlocks();
        LOGGER.info("Registered Blocks of" + MOD_ID);
        LOGGER.info("Initializing Packets of" + MOD_ID);
        ModNetworking.register();
        LOGGER.info("Registered Packets of" + MOD_ID);

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                RegenerationAmulet.implementAmulet(player);
                ResistanceAmulet.implementAmulet(player);
                SpeedAmulet.implementAmulet(player);
                StrengthAmulet.implementAmulet(player);

                if (Launcher.lowFrictionPlayers.contains(player)) {
                    Vec3d velocity = player.getVelocity();
                    if (!player.isOnGround() && !player.isTouchingWater() && !player.isInLava()) {
                        if (velocity.y > 0) { // only apply boost when still rising
                            player.setVelocity(velocity.multiply(1.05));
                            player.velocityModified = true;
                        }
                    } else {
                        Launcher.lowFrictionPlayers.remove(player);
                    }
                }
            }
        });
	}
}