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
import com.industry.serverSideImplementers.GlobalTickHandler;
import com.industry.serverSideImplementers.PlayerTickHandler;
import com.industry.serverSideImplementers.Registers;
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

        Registers.registerAll();

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            GlobalTickHandler.tick();

            for (PlayerEntity player : world.getPlayers()) {
                PlayerTickHandler.tick(player);
            }
        });
	}
}