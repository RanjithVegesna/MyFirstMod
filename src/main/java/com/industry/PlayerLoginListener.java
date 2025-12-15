package com.industry;

import classes.Row;
import classes.Table;
import com.industry.item.WorldEditAxe;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;

import java.util.List;
import java.util.UUID;

import static com.industry.Mod.*;

public class PlayerLoginListener {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
                playerDataTable.add(player.getUuid(), WorldEditAxe.NONE, WorldEditAxe.NONE);
                playerEntityMap.put(player.getUuid(), player);

            });
        };
}
