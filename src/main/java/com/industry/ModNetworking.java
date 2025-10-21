package com.industry;

import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModNetworking {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(RailgunFlagsPayload.ID, RailgunFlagsPayload.CODEC);
    }

    public static void sendRailgunFlags(ServerPlayerEntity player, boolean flag1, int flag2) {
        ServerPlayNetworking.send(player, new RailgunFlagsPayload(flag1, flag2));
    }
}
