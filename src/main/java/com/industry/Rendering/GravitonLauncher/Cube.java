package com.industry.Rendering.GravitonLauncher;

import com.industry.Rendering.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import static com.industry.textures.ModTextures.White;

public class Cube {
    public double size;
    private Vec3d vertex;
    private Vec3d center;

    Cube(Vec3d vertex, Vec3d center) {
        this.vertex = vertex;
        this.center = center;
        this.size = center.distanceTo(vertex) / Math.sqrt(3);
    }

    public void setSize(double newSize) {
        Vec3d offset = vertex.subtract(center);
        double scale = newSize / this.size;
        offset = offset.multiply(scale);
        vertex = center.add(offset);
        this.size = newSize;
    }
    public void rotate(Quaterniond quaternion) {
        Vector3d offset = new Vector3d(vertex.x - center.x, vertex.y - center.y, vertex.z - center.z);

        quaternion.transform(offset);

        vertex = new Vec3d(center.x + offset.x, center.y + offset.y, center.z + offset.z);
    }

    public void render(VertexConsumerProvider vertexConsumers, MatrixStack matrices, float r, float g, float b, float a) {
        // specific order for renderBox
        Vec3d offset = vertex.subtract(center);

        double dx = offset.x;
        double dy = offset.y;
        double dz = offset.z;

        Vec3d p1 = new Vec3d(center.x - dx, center.y - dy, center.z - dz);
        Vec3d p2 = new Vec3d(center.x - dx, center.y + dy, center.z - dz);
        Vec3d p3 = new Vec3d(center.x + dx, center.y + dy, center.z - dz);
        Vec3d p4 = new Vec3d(center.x + dx, center.y - dy, center.z - dz);

        Vec3d p5 = new Vec3d(center.x - dx, center.y - dy, center.z + dz);
        Vec3d p6 = new Vec3d(center.x - dx, center.y + dy, center.z + dz);
        Vec3d p7 = new Vec3d(center.x + dx, center.y + dy, center.z + dz);
        Vec3d p8 = new Vec3d(center.x + dx, center.y - dy, center.z + dz);

        RenderUtil.renderBox(vertexConsumers, matrices.peek().getPositionMatrix(), White, p1, p2, p3, p4, p5, p6, p7, p8, r, g, b, a, false, MinecraftClient.getInstance());
    }
}
