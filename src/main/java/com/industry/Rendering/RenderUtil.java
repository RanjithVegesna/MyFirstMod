package com.industry.Rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderUtil {

    public static VertexConsumer vertexConsumer(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y, float z, float r, float g, float b, float a, float u, float v, Vec3d normal) {
        return vertexConsumer.vertex(matrix, x, y, z)
                .color(r, g, b, a)
                .texture(u, v)
                .overlay(0, 0)
                .light(0xF000F0)
                .normal((float) normal.x, (float) normal.y, (float) normal.z);
    }

    // --- Renders a single triangle with proper normal and UV mapping ---
    public static void renderTriangle(VertexConsumer vertexConsumer, Matrix4f matrix,
                                      Vec3d p1, Vec3d p2, Vec3d p3,
                                      float r, float g, float b, float a) {

        Vec3d edge1 = p2.subtract(p1);
        Vec3d edge2 = p3.subtract(p1);
        Vec3d normalVec = edge1.crossProduct(edge2).normalize();

        float nx = (float) normalVec.x;
        float ny = (float) normalVec.y;
        float nz = (float) normalVec.z;


        // Use your vertexConsumer helper for all three vertices
        vertexConsumer(vertexConsumer, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, normalVec);
        vertexConsumer(vertexConsumer, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 1f, 1f, normalVec);
        vertexConsumer(vertexConsumer, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 0f, 0f, normalVec);

    }

    public static void renderQuad(VertexConsumer vertex, Matrix4f matrix,
                                  Vec3d p1,  Vec3d p2, Vec3d p3, Vec3d p4, float r, float g, float b, float a
    ) {
        RenderSystem.disableCull();
        Vec3d normal = p2.subtract(p1).crossProduct(p3.subtract(p1)).normalize();


        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, normal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, normal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 1f, 1f, normal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 1f, 0f, normal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, normal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, normal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 1f, 0f, normal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 0f, normal);
        RenderSystem.enableCull();
    }

    // --- Renders a textured box given 8 corner points ---
    public static void renderBox(VertexConsumer vertex, Matrix4f matrix,
                                 Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4,
                                 Vec3d p5, Vec3d p6, Vec3d p7, Vec3d p8,
                                 float r, float g, float b, float a
    ) {

        RenderSystem.disableCull();

        // Compute normals for each face
        Vec3d frontNormal = p2.subtract(p1).crossProduct(p3.subtract(p1)).normalize();
        Vec3d backNormal = p6.subtract(p5).crossProduct(p7.subtract(p5)).normalize();
        Vec3d topNormal = p2.subtract(p1).crossProduct(p5.subtract(p1)).normalize();
        Vec3d bottomNormal = p4.subtract(p3).crossProduct(p7.subtract(p3)).normalize();
        Vec3d leftNormal = p5.subtract(p1).crossProduct(p4.subtract(p1)).normalize();
        Vec3d rightNormal = p2.subtract(p6).crossProduct(p7.subtract(p6)).normalize();

        // Front face (p1,p2,p3,p4)
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 1f, 1f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 1f, 0f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 1f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 1f, 0f, frontNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 0f, frontNormal);

        // Back face (p5,p6,p7,p8)
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 1f, backNormal);
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 1f, backNormal);
        vertexConsumer(vertex, matrix, (float) p7.x, (float) p7.y, (float) p7.z, r, g, b, a, 1f, 1f, backNormal);
        vertexConsumer(vertex, matrix, (float) p6.x, (float) p6.y, (float) p6.z, r, g, b, a, 1f, 0f, backNormal);
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 1f, backNormal);
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 1f, backNormal);
        vertexConsumer(vertex, matrix, (float) p6.x, (float) p6.y, (float) p6.z, r, g, b, a, 1f, 0f, backNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 0f, backNormal);

        // Top face (p5,p6,p2,p1)
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, topNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, topNormal);
        vertexConsumer(vertex, matrix, (float) p6.x, (float) p6.y, (float) p6.z, r, g, b, a, 1f, 1f, topNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 1f, 0f, topNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, topNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, topNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 1f, 0f, topNormal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 0f, 0f, topNormal);

        // Bottom face (p4,p3,p7,p8)
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 1f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 1f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 1f, 1f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p7.x, (float) p7.y, (float) p7.z, r, g, b, a, 1f, 0f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 1f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 0f, 1f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p7.x, (float) p7.y, (float) p7.z, r, g, b, a, 1f, 0f, bottomNormal);
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 0f, bottomNormal);

        // Left face (p5,p1,p4,p8)
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p1.x, (float) p1.y, (float) p1.z, r, g, b, a, 1f, 1f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 1f, 0f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p5.x, (float) p5.y, (float) p5.z, r, g, b, a, 0f, 1f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p4.x, (float) p4.y, (float) p4.z, r, g, b, a, 1f, 0f, leftNormal);
        vertexConsumer(vertex, matrix, (float) p8.x, (float) p8.y, (float) p8.z, r, g, b, a, 0f, 0f, leftNormal);

        // Right face (p2,p6,p7,p3)
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 0f, 1f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 0f, 1f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p6.x, (float) p6.y, (float) p6.z, r, g, b, a, 1f, 1f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p7.x, (float) p7.y, (float) p7.z, r, g, b, a, 1f, 0f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 0f, 1f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p2.x, (float) p2.y, (float) p2.z, r, g, b, a, 0f, 1f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p7.x, (float) p7.y, (float) p7.z, r, g, b, a, 1f, 0f, rightNormal);
        vertexConsumer(vertex, matrix, (float) p3.x, (float) p3.y, (float) p3.z, r, g, b, a, 0f, 0f, rightNormal);
        RenderSystem.enableCull();
    }

    public static VertexConsumer getTextureConsumer(VertexConsumerProvider vertices, Identifier texture) {
        // Use translucent entity render layer
        RenderLayer layer = RenderLayer.getEntityAlpha(texture);
        return vertices.getBuffer(layer);
    }
}
