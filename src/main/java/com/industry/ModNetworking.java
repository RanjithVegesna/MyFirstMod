package com.industry;

import com.industry.packets.GravitonLauncherPayload;
import com.industry.packets.OrbitalLazerCannonPayload;
import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ModNetworking {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(RailgunFlagsPayload.ID, RailgunFlagsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OrbitalLazerCannonPayload.ID, OrbitalLazerCannonPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GravitonLauncherPayload.ID, GravitonLauncherPayload.CODEC);
    }
}
