package com.industry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;


import static com.industry.textures.ModTextures.*;

public class RenderUtil {

    public static void renderTriangle(VertexConsumer vertexConsumer, Matrix4f matrix,
                                      Vec3d p1, Vec3d p2, Vec3d p3,
                                      float r, float g, float b, float a) {

        // Calculate normal vector
        Vec3d edge1 = p2.subtract(p1);
        Vec3d edge2 = p3.subtract(p1);
        Vec3d normalVec = edge1.crossProduct(edge2).normalize();

        float nx = (float) normalVec.x;
        float ny = (float) normalVec.y;
        float nz = (float) normalVec.z;

        vertexConsumer.vertex(matrix, (float) p1.x, (float) p1.y, (float) p1.z)
                .color(r, g, b, a)
                .texture(0f, 1f)
                .overlay(0, 0)
                .light(0xF000F0, 0xF000F0)
                .normal(nx, ny, nz);


        vertexConsumer.vertex(matrix, (float) p2.x, (float) p2.y, (float) p2.z)
                .color(r, g, b, a)
                .texture(1f, 1f)
                .overlay(0, 0)
                .light(0xF000F0, 0xF000F0)
                .normal(nx, ny, nz);


        vertexConsumer.vertex(matrix, (float) p3.x, (float) p3.y, (float) p3.z)
                .color(r, g, b, a)
                .texture(1f, 0f)
                .overlay(0, 0)
                .light(0xF000F0, 0xF000F0)
                .normal(nx, ny, nz);

    }

    public static void renderDoubleSidedQuad(VertexConsumer vertexConsumer, Matrix4f matrix,
                                             Vec3d a, Vec3d b, Vec3d c, Vec3d d,
                                             float r, float g, float bCol, float aCol) {
        renderQuad(vertexConsumer, matrix, a, b, c, d, r, g, bCol, aCol);
        renderQuad(vertexConsumer, matrix, d, c, b, a, r, g, bCol, aCol);
    }

    public static void renderQuad(VertexConsumer vertexConsumer, Matrix4f matrix,
                                  Vec3d a, Vec3d b, Vec3d c, Vec3d d,
                                  float r, float g, float bCol, float aCol) {
        renderTriangle(vertexConsumer, matrix, a, c, d, r, g, bCol, aCol);
        renderTriangle(vertexConsumer, matrix, d, c, a, r, g, bCol, aCol);
    }

    public static void renderVertexLabel(VertexConsumerProvider vertexConsumers, Matrix4f matrix,
                                         MatrixStack matrixStack,
                                         Vec3d pos, String label,
                                         float size,
                                         float r, float g, float b, float a) {

        // Render a small cube (marker)
        Vec3d p1 = pos.subtract(new Vec3d(size, size, size));  // min corner
        Vec3d p2 = pos.add(new Vec3d(size, size, size));       // max corner

        renderBox(vertexConsumers, matrix,
                Blue,
                // top face
                new Vec3d(p1.x, p2.y, p1.z),   // p1 top-left-front
                new Vec3d(p1.x, p1.y, p1.z),   // p2 bottom-left-front
                new Vec3d(p2.x, p1.y, p1.z),   // p3 bottom-right-front
                new Vec3d(p2.x, p2.y, p1.z),   // p4 top-right-front

                // bottom face
                new Vec3d(p2.x, p2.y, p2.z),   // p5 top-right-back
                new Vec3d(p2.x, p1.y, p2.z),   // p6 bottom-right-back
                new Vec3d(p1.x, p1.y, p2.z),   // p7 bottom-left-back
                new Vec3d(p1.x, p2.y, p2.z),   // p8 top-left-back

                r, g, b, a);


        // Render label text above the vertex
        matrixStack.push();

        // Translate to the vertex position
        matrixStack.translate(pos.x, pos.y + size * 2, pos.z);

        // Get Minecraft's text renderer
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        // Rotate text to always face the camera
        matrixStack.multiply(MinecraftClient.getInstance().getEntityRenderDispatcher().getRotation());

        // Scale down the text (optional, tweak as needed)
        float scale = 1f;
        matrixStack.scale(-scale, -scale, scale);

        // Draw the text centered
        textRenderer.draw(
                Text.of(label),
                -textRenderer.getWidth(label) / 2f,
                0,
                0xFFFFFF, // white color
                false,
                matrixStack.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.SEE_THROUGH,
                0,
                15728880 // lighting
        );

        matrixStack.pop();
    }


    public static void renderBox(VertexConsumerProvider vertexConsumers, Matrix4f matrix,
                                 Identifier texture,
                                 Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4,
                                 Vec3d p5, Vec3d p6, Vec3d p7, Vec3d p8,
                                 float r, float g, float b, float a) {

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));

        // FRONT face (z-)
        renderDoubleSidedQuad(vertexConsumer, matrix, p1, p2, p3, p4, r, g, b, a);

        // BACK face (z+)
        renderDoubleSidedQuad(vertexConsumer, matrix, p5, p6, p7, p8, r, g, b, a);

        // TOP face (y+)
        renderDoubleSidedQuad(vertexConsumer, matrix, p8, p1, p4, p5, r, g, b, a);

        // BOTTOM face (y-)
        renderDoubleSidedQuad(vertexConsumer, matrix, p7, p2, p3, p6, r, g, b, a);

        // LEFT face (x-)
        renderDoubleSidedQuad(vertexConsumer, matrix, p1, p2, p6, p5, r, g, b, a);

        // RIGHT face (x+)
        renderDoubleSidedQuad(vertexConsumer, matrix, p4, p3, p7, p8, r, g, b, a);
    }
}