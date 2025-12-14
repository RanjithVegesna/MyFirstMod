package com.industry.Rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import static com.industry.Rendering.RenderUtil.renderBox;

public class DebugRender {
    // Example texture for the cube
    private static final Identifier CUBE_TEXTURE = Identifier.of("minecraft", "textures/block/bedrock.png");

    public static void render(VertexConsumerProvider vertexConsumers, MatrixStack matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;

        if (world == null || player == null) return;

        Vec3d eyePos = player.getCameraPosVec(1.0f);
        Vec3d lookingVec = player.getRotationVec(1.0f);
        double distance = 2.0; // blocks in front of eyes
        double size = 1.5;

        Vec3d cubeCenter = eyePos.add(lookingVec.multiply(distance));
        double hs = size / 2;

// Cube corners in world space
        Vec3d p1 = cubeCenter.add(-hs, -hs, -hs);
        Vec3d p2 = cubeCenter.add(hs, -hs, -hs);
        Vec3d p3 = cubeCenter.add(hs, -hs, hs);
        Vec3d p4 = cubeCenter.add(-hs, -hs, hs);
        Vec3d p5 = cubeCenter.add(-hs, hs, -hs);
        Vec3d p6 = cubeCenter.add(hs, hs, -hs);
        Vec3d p7 = cubeCenter.add(hs, hs, hs);
        Vec3d p8 = cubeCenter.add(-hs, hs, hs);

// Subtract camera position to make cube camera-relative
        p1 = p1.subtract(eyePos);
        p2 = p2.subtract(eyePos);
        p3 = p3.subtract(eyePos);
        p4 = p4.subtract(eyePos);
        p5 = p5.subtract(eyePos);
        p6 = p6.subtract(eyePos);
        p7 = p7.subtract(eyePos);
        p8 = p8.subtract(eyePos);
        // Get the current transformation matrix
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        // Render the cube with RGBA color (1,0,0,1) = red
        renderBox(vertexConsumers, matrix, CUBE_TEXTURE, p1, p2, p3, p4, p5, p6, p7, p8, 1f, 1f, 1f, 1f);
    }
}