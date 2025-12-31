package com.industry.serverSideImplementers;

import com.industry.*;
import com.industry.Blocks.ModBlockEntities;
import com.industry.Blocks.ModBlocks;
import com.industry.Commands.ModCommands;
import com.industry.item.ModItems;

import java.util.List;

import static com.industry.Mod.*;

public class Registers {
    public static void registerAll() {
        playerDataTable.nameColumns(List.of("UUID", "Pos1", "Pos2"));
        PlayerLeaveListener.register();
        PlayerLoginListener.register();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerModBlockEntities();
        ModNetworking.register();
        ModCommands.register();
        ModEvents.register();
        ModSoundEvents.register();
    }
}
