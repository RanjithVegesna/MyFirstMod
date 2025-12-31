package com.industry.Rendering;

import com.industry.Blocks.BeaconBeamTrapBlock;
import com.industry.Blocks.BeaconBeamTrapBlockEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;

import static com.industry.Rendering.RenderUtil.renderBox;
import static com.industry.Rendering.RenderUtil.renderQuad;
import static com.industry.textures.ModTextures.*;

public class BeaconBeamTrapRender implements BlockEntityRenderer<BeaconBeamTrapBlockEntity> {

    public BeaconBeamTrapRender(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(
            BeaconBeamTrapBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertices,
            int light,
            int overlay
    ) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world == null) return;

        double beamWidth = 0.1 + Math.sin(client.world.getTimeOfDay() / 24.0) * 0.01;
        double maxDistance= 100;
        double half = 0.5;
        Vec3d startT = entity.getPos().toCenterPos();
        Vec3d directionT = Vec3d.of(entity.getCachedState().get(BeaconBeamTrapBlock.FACING).getVector());
        Vec3d endT = startT.add(directionT.multiply(maxDistance));


        BlockHitResult hitResult = client.world.raycast(
                new RaycastContext(
                        startT.add(directionT), endT,
                        RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE,
                        ShapeContext.absent()
                )
        );

        double beamLength = maxDistance;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            beamLength = hitResult.getPos().distanceTo(startT);
        }

        Direction direction = entity.getCachedState().get(BeaconBeamTrapBlock.FACING);

        matrices.push();

        // Move origin to block center (BER is already camera-relative)
        matrices.translate(0.5, 0.5, 0.5);

        // Local-space start and end
        Vec3d start = Vec3d.ZERO;
        Vec3d end = Vec3d.of(direction.getVector()).multiply(beamLength);

        Vec3d dir = end.subtract(start);
        Vec3d normDir = dir.normalize();

        // Build perpendicular vectors
        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = normDir.crossProduct(up);

        if (right.lengthSquared() < 1e-6) {
            up = new Vec3d(1, 0, 0);
            right = normDir.crossProduct(up);
        }

        right = right.normalize().multiply(beamWidth);
        Vec3d upAdjusted = right.crossProduct(normDir).normalize().multiply(beamWidth);

        // Start face
        Vec3d p1 = start.subtract(right).add(upAdjusted);
        Vec3d p2 = start.subtract(right).subtract(upAdjusted);
        Vec3d p3 = start.add(right).subtract(upAdjusted);
        Vec3d p4 = start.add(right).add(upAdjusted);

        // End face
        Vec3d p5 = end.subtract(right).add(upAdjusted);
        Vec3d p6 = end.subtract(right).subtract(upAdjusted);
        Vec3d p7 = end.add(right).subtract(upAdjusted);
        Vec3d p8 = end.add(right).add(upAdjusted);

        renderBox(
                vertices.getBuffer(RenderLayer.getEntityTranslucent(Red)),
                matrices.peek().getPositionMatrix(),
                p1, p2, p3, p4,
                p5, p6, p7, p8,
                1f, 0f, 0f, 1f
        );

        Vec3d worldUp = new Vec3d(0, 1, 0);
        if (Math.abs(directionT.dotProduct(worldUp)) > 0.999) {
            worldUp = new Vec3d(1, 0, 0);
        }

        Vec3d rightVec = directionT.crossProduct(worldUp).normalize();
        Vec3d frontVec = Vec3d.of(entity.getCachedState().get(BeaconBeamTrapBlock.FACING).getVector()).normalize();
        Vec3d upVec = rightVec.crossProduct(directionT).normalize();

        Vec3d faceCenter = directionT.multiply(half);
        Vec3d topRight = faceCenter.add(rightVec.multiply(half)).add(upVec.multiply(half));
        Vec3d depth = frontVec.multiply(-1.0);

        p1 = topRight;
        p2 = topRight.add(upVec.multiply(-1));
        p3 = topRight.add(upVec.multiply(-1).add(rightVec.multiply(-1)));
        p4 = topRight.add(rightVec.multiply(-1));

        p5 = p4.add(depth);
        p6 = p3.add(depth);
        p7 = p2.add(depth);
        p8 = p1.add(depth);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockFront)), matrix, p1, p2, p3, p4, 1, 1, 1, 1);
        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockBack)), matrix, p6, p7, p8, p5, 1, 1, 1, 1);

        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockSide)), matrix, p4, p3, p6, p5, 1, 1, 1, 1);
        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockSide)), matrix, p8, p7, p2, p1, 1, 1, 1, 1);
        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockSide)), matrix, p1, p4, p5, p8, 1, 1, 1, 1);
        renderQuad(vertices.getBuffer(RenderLayer.getEntityAlpha(BeamBlockSide)), matrix, p7, p6, p3, p2, 1, 1, 1, 1);

        matrices.pop();
    }
}
