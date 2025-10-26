package com.industry.packets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.minecraft.util.math.Vec3d;

public class RailgunFlagsPayload implements CustomPayload {

    public final boolean flag1;
    public final int flag2;
    public final Vec3d start;
    public final Vec3d end;

    public RailgunFlagsPayload(boolean flag1, int flag2, Vec3d start, Vec3d end) {
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.start = start;
        this.end = end;
    }

    public static final CustomPayload.Id<RailgunFlagsPayload> ID =
            new CustomPayload.Id<>(Identifier.of("industry", "railgun_flags"));
    public static final PacketCodec<PacketByteBuf, RailgunFlagsPayload> CODEC =
            PacketCodec.of(
                    (RailgunFlagsPayload payload, PacketByteBuf buf) -> {
                        buf.writeBoolean(payload.flag1);
                        buf.writeInt(payload.flag2);
                        buf.writeDouble(payload.start.x);
                        buf.writeDouble(payload.start.y);
                        buf.writeDouble(payload.start.z);
                        buf.writeDouble(payload.end.x);
                        buf.writeDouble(payload.end.y);
                        buf.writeDouble(payload.end.z);
                    },
                    (PacketByteBuf buf) -> {
                        boolean flag1 = buf.readBoolean();
                        int flag2 = buf.readInt();
                        double sx = buf.readDouble();
                        double sy = buf.readDouble();
                        double sz = buf.readDouble();
                        double ex = buf.readDouble();
                        double ey = buf.readDouble();
                        double ez = buf.readDouble();
                        return new RailgunFlagsPayload(flag1, flag2, new Vec3d(sx, sy, sz), new Vec3d(ex, ey, ez));
                    }
            );


    public boolean Flag1() { return this.flag1; }
    public int Flag2() { return this.flag2; }
    public Vec3d Start() { return this.start; }
    public Vec3d End() { return this.end; }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
