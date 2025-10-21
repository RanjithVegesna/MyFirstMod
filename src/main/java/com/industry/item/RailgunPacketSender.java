package com.industry.item;

import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class RailgunPacketSender {

    public static void sendFlags(ServerPlayerEntity player, boolean flag1, int flag2) {
        RailgunFlagsPayload payload = new RailgunFlagsPayload(flag1, flag2);
        ServerPlayNetworking.send(player, payload); // send the entire payload object
    }
}
