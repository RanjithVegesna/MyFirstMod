package com.industry.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class GravitonLauncherPayload implements CustomPayload {
    public final Vec3d vec;


    public GravitonLauncherPayload(Vec3d start) {
        this.vec = start;

    }

    public static final CustomPayload.Id<GravitonLauncherPayload> ID =
            new CustomPayload.Id<>(Identifier.of("industry", "graviton_launcher"));
    public static final PacketCodec<PacketByteBuf, GravitonLauncherPayload> CODEC =
            PacketCodec.of(
                    (GravitonLauncherPayload payload, PacketByteBuf buf) -> {
                        buf.writeDouble(payload.vec.x);
                        buf.writeDouble(payload.vec.y);
                        buf.writeDouble(payload.vec.z);
                    },
                    (PacketByteBuf buf) -> {
                        double sx = buf.readDouble();
                        double sy = buf.readDouble();
                        double sz = buf.readDouble();
                        return new GravitonLauncherPayload(new Vec3d(sx, sy, sz));
                    }
            );

    public Vec3d Vec() { return this.vec; }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
