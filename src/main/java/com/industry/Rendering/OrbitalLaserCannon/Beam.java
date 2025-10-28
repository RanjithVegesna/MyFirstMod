package com.industry.Rendering.OrbitalLaserCannon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import static com.industry.Rendering.RenderUtil.renderBox;
import static com.industry.textures.ModTextures.Red;

public class Beam{
    public double x;
    public double z;
    public double moveX;
    public double moveZ;

    Beam(Vec3d vector, Vec3d movementVector) {
        x = vector.x;
        z = vector.z;
        moveX = movementVector.x;
        moveZ = movementVector.z;
    }

    public void renderBeam(VertexConsumerProvider vertexConsumers, MatrixStack matrices, double size) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        // Convert from world → camera space once here
        Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
        Vec3d relPos = new Vec3d(x, 0, z).subtract(cameraPos);

        double yTop = 319;
        double yBottom = -64;

        Vec3d p1 = relPos.add(size, yTop, -size);
        Vec3d p2 = relPos.add(-size, yTop, -size);
        Vec3d p3 = relPos.add(-size, yTop, size);
        Vec3d p4 = relPos.add(size, yTop, size);
        Vec3d p5 = relPos.add(size, yBottom, size);
        Vec3d p6 = relPos.add(-size, yBottom, size);
        Vec3d p7 = relPos.add(-size, yBottom, -size);
        Vec3d p8 = relPos.add(size, yBottom, -size);

        renderBox(vertexConsumers, matrices.peek().getPositionMatrix(), Red,
                p1, p2, p3, p4, p5, p6, p7, p8,
                1, 0, 0, 1, false, client);


    }
    public void moveBeam(Vec3d vector) {
        x += vector.x;
        z += vector.z;
    }

    public void moveBeam() {
        x += moveX;
        z += moveZ;
    }

    public static enum State {
        NONE,
        BEAM,
        SPLITTING,
        AFTER_SPLIT
    }
}
