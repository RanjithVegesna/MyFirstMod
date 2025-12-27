package com.industry.serverSideImplementers;

import com.industry.Blocks.ModBlockEntities;
import com.industry.Blocks.ModBlocks;
import com.industry.Commands.ModCommands;
import com.industry.ModNetworking;
import com.industry.PlayerLeaveListener;
import com.industry.PlayerLoginListener;
import com.industry.item.ModItems;

import java.util.List;

import static com.industry.Mod.*;

public class Registers {
    public static void registerAll() {
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
    }
}
