package com.industry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

import static com.industry.RenderUtil.renderBox;
import static com.industry.textures.ModTextures.White;

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

        // Camera-relative position
        Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
        Vec3d relPos = new Vec3d(x, 0, z).subtract(cameraPos);

        // Top vertices (world height 319)
        Vec3d p1 = new Vec3d(x + size, 319 - cameraPos.y, z - size);
        Vec3d p2 = new Vec3d(x - size, 319 - cameraPos.y, z - size);
        Vec3d p3 = new Vec3d(x - size, 319 - cameraPos.y, z + size);
        Vec3d p4 = new Vec3d(x + size, 319 - cameraPos.y, z + size);

        // Bottom vertices (world height -64)
        Vec3d p5 = new Vec3d(x + size, -64 - cameraPos.y, z + size);
        Vec3d p6 = new Vec3d(x - size, -64 - cameraPos.y, z + size);
        Vec3d p7 = new Vec3d(x - size, -64 - cameraPos.y, z - size);
        Vec3d p8 = new Vec3d(x + size, -64 - cameraPos.y, z - size);

        // Render the vertical box
        renderBox(vertexConsumers, matrices.peek().getPositionMatrix(), White, p1, p2, p3, p4, p5, p6, p7, p8, 1, 0, 0, 1, true, MinecraftClient.getInstance());

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
