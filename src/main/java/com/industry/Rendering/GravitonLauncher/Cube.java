package com.industry.Rendering.GravitonLauncher;

import com.industry.math.Matrix3;
import com.industry.math.Vector3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import static com.industry.Rendering.RenderUtil.renderBox;
import static com.industry.textures.ModTextures.Graviton;
import static com.industry.textures.ModTextures.White;

public class Cube {

    private Vector3 p1, p2, p3, p4, p5, p6, p7, p8;
    private Vector3 center;
    private double size;

    public Cube(Vector3 center, double size) {
        this.center = center;
        this.size = size;
        updateCorners();
    }

    /**
     * Rotates the cube around its center in world coordinates.
     *
     * @param axis  rotation axis
     * @param angle rotation angle (radians)
     * @param twist optional twist angle
     */
    public void rotate(Vector3 axis, double angle, double twist) {
        Matrix3 matrix = Matrix3.getTwistMatrix(axis, angle, twist);

        // Rotate each corner relative to the center
        p1 = matrix.transform(p1.subtract(center)).add(center);
        p2 = matrix.transform(p2.subtract(center)).add(center);
        p3 = matrix.transform(p3.subtract(center)).add(center);
        p4 = matrix.transform(p4.subtract(center)).add(center);
        p5 = matrix.transform(p5.subtract(center)).add(center);
        p6 = matrix.transform(p6.subtract(center)).add(center);
        p7 = matrix.transform(p7.subtract(center)).add(center);
        p8 = matrix.transform(p8.subtract(center)).add(center);
    }

    /**
     * Updates the corner positions of the cube based on current center and size.
     */
    private void updateCorners() {
        double half = size / 2;

        // Corners in world coordinates
        p1 = center.add(new Vector3(-half, -half, -half)); // minX, minY, minZ
        p2 = center.add(new Vector3(-half, +half, -half)); // minX, maxY, minZ
        p3 = center.add(new Vector3(+half, +half, -half)); // maxX, maxY, minZ
        p4 = center.add(new Vector3(+half, -half, -half)); // maxX, minY, minZ

        p5 = center.add(new Vector3(-half, -half, +half)); // minX, minY, maxZ
        p6 = center.add(new Vector3(-half, +half, +half)); // minX, maxY, maxZ
        p7 = center.add(new Vector3(+half, +half, +half)); // maxX, maxY, maxZ
        p8 = center.add(new Vector3(+half, -half, +half)); // maxX, minY, maxZ
    }

    /**
     * Sets a new cube size and updates corners.
     */
    public void setSize(double newSize) {
        this.size = newSize;
        updateCorners();
    }

    /**
     * Updates the cube's center (world coordinates) and recalculates corners.
     */
    public void setCenter(Vector3 newCenter) {
        this.center = newCenter;
        updateCorners();
    }

    /**
     * Renders the cube using world coordinates.
     */
    public void render(VertexConsumerProvider vertexConsumers, MatrixStack matrices, float r, float g, float b, float a) {
        Vec3d cameraPos = MinecraftClient.getInstance().player.getCameraPosVec(1.0F);
        renderBox(
                vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(Graviton)),
                matrices.peek().getPositionMatrix(),
                p1.toVec3d().subtract(cameraPos), p2.toVec3d().subtract(cameraPos), p3.toVec3d().subtract(cameraPos), p4.toVec3d().subtract(cameraPos),
                p5.toVec3d().subtract(cameraPos), p6.toVec3d().subtract(cameraPos), p7.toVec3d().subtract(cameraPos), p8.toVec3d().subtract(cameraPos),
                r, g, b, a
                );
    }
}
