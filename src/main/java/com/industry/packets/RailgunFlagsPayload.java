package com.industry.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RailgunFlagsPayload(boolean flag1, int flag2) implements CustomPayload {
    public static final CustomPayload.Id<RailgunFlagsPayload> ID = new CustomPayload.Id<>(Identifier.of("industry", "railgun_flags"));

    public static final PacketCodec<PacketByteBuf, RailgunFlagsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, RailgunFlagsPayload::flag1,
            PacketCodecs.INTEGER, RailgunFlagsPayload::flag2,
            RailgunFlagsPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
