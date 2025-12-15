package com.industry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

import static com.industry.Mod.*;

public class PlayerLeaveListener {
    public static void register(){
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            UUID uuid = player.getUuid();

            // Remove from your map
            playerDataTable.delete(uuid, "UUID");
            playerEntityMap.remove(uuid);
        });
    }
}
