package com.industry;

import classes.Table;
import com.industry.Blocks.ModBlockEntities;
import com.industry.Blocks.ModBlocks;
import com.industry.Commands.ModCommands;
import com.industry.item.Launcher;
import com.industry.item.ModItems;
import com.industry.item.amulet.RegenerationAmulet;
import com.industry.item.amulet.ResistanceAmulet;
import com.industry.item.amulet.SpeedAmulet;
import com.industry.item.amulet.StrengthAmulet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.industry.MaterialsAndTools.ModMaterials.LifeSteal;


public class Mod implements ModInitializer {
	public static final String MOD_ID = "industry";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean hit = false;

    public static Table playerDataTable = new Table(List.of(UUID.class, BlockPos.class, BlockPos.class));
    public static HashMap<UUID, PlayerEntity> playerEntityMap = new HashMap<>();

	@Override
	public void onInitialize() {

        System.out.println(LifeSteal);
        playerDataTable.nameColumns(List.of("UUID", "Pos1", "Pos2"));
        PlayerLeaveListener.register();
        PlayerLoginListener.register();
        LOGGER.info("Initializing Items of" + MOD_ID);
        ModItems.registerModItems();
        LOGGER.info("Registered Items of" + MOD_ID);
        LOGGER.info("Initializing Blocks of" + MOD_ID);
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        LOGGER.info("Registered Blocks of" + MOD_ID);
        LOGGER.info("Initializing Packets of" + MOD_ID);
        ModNetworking.register();
        LOGGER.info("Registered Packets of" + MOD_ID);
        ModCommands.register();

        ServerTickEvents.END_WORLD_TICK.register(world -> {

            for (PlayerEntity player : world.getPlayers()) {
                player.getItemCooldownManager().remove(Items.WIND_CHARGE);

                RegenerationAmulet.implementAmulet(player);
                ResistanceAmulet.implementAmulet(player);
                SpeedAmulet.implementAmulet(player);
                StrengthAmulet.implementAmulet(player);

                if (Launcher.lowFrictionPlayers.contains(player)) {
                    Vec3d velocity = player.getVelocity();
                    if (!player.isOnGround() && !player.isTouchingWater() && !player.isInLava()) {
                        if (velocity.y > 0) {
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