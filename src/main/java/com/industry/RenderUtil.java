package com.industry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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

    public static void renderBox(VertexConsumerProvider vertexConsumers, Matrix4f matrix,
                                 Identifier texture,
                                 Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4,
                                 Vec3d p5, Vec3d p6, Vec3d p7, Vec3d p8,
                                 float r, float g, float b, float a,
                                 boolean collideWithBlocks, MinecraftClient client) {

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));

        // Helper: skip a quad if all corners are inside solid blocks
        java.util.function.Consumer<Vec3d[]> renderQuadIfAir = (Vec3d[] corners) -> {
            if (collideWithBlocks) {
                for (Vec3d corner : corners) {
                    BlockPos blockPos = new BlockPos((int) corner.x,  (int) corner.y, (int) corner.z);
                    if (!client.world.isAir(blockPos)) return; // skip this quad
                }
            }
            renderDoubleSidedQuad(vertexConsumer, matrix, corners[0], corners[1], corners[2], corners[3], r, g, b, a);
        };

        // FRONT face
        renderQuadIfAir.accept(new Vec3d[]{p1, p2, p3, p4});
        // BACK face
        renderQuadIfAir.accept(new Vec3d[]{p5, p6, p7, p8});
        // TOP face
        renderQuadIfAir.accept(new Vec3d[]{p8, p1, p4, p5});
        // BOTTOM face
        renderQuadIfAir.accept(new Vec3d[]{p7, p2, p3, p6});
        // LEFT face
        renderQuadIfAir.accept(new Vec3d[]{p1, p2, p6, p5});
        // RIGHT face
        renderQuadIfAir.accept(new Vec3d[]{p4, p3, p7, p8});
    }
}