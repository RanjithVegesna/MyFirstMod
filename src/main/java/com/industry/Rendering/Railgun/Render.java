package com.industry.Rendering.Railgun;

import com.industry.Rendering.RenderUtil;
import com.industry.item.RailgunClientState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import static com.industry.Rendering.RenderUtil.renderBox;
import static com.industry.textures.ModTextures.White;

public class Render {

    public static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        if (!RailgunClientState.isBeamActive()) {
            return;
        }

        RailgunClientState.BeamPhase phase = RailgunClientState.getPhase();
        Vec3d start = RailgunClientState.getStartPos();
        Vec3d end = RailgunClientState.getEndPos();
        boolean hit = RailgunClientState.didHit();

        if (phase == RailgunClientState.BeamPhase.CHARGING) {
            float expandProgress = RailgunClientState.getChargeProgress();

            ItemStack itemStack = MinecraftClient.getInstance().player.getMainHandStack();
            Item itemInHand = itemStack.getItem();
            Vec3d handPosOffset = MinecraftClient.getInstance().player.getHandPosOffset(itemInHand);  // whichever item your player is holding
            Vec3d lookVec = MinecraftClient.getInstance().player.getRotationVec(1);
            Vec3d cameraPos = MinecraftClient.getInstance().player.getCameraPosVec(1).add(handPosOffset).add(lookVec).add(0, -0.1, 0);

            start = cameraPos;
            double maxLength = MinecraftClient.getInstance().options.getViewDistance().getValue(); // Max beam length during charging
            end = start.add(lookVec.multiply(maxLength * 16));

            // Light blue color for charging beam
            float red = 0f;
            float green = 1f;
            float blue = 1f;
            float alpha = 0.85f;

            float beamThickness = 0.1f * expandProgress;

            renderBeamBox(matrices, vertexConsumers, start, end, red, green, blue, alpha, beamThickness);
        }
        else if (phase == RailgunClientState.BeamPhase.RESULT) {
            // Use stored start and end positions
            if (start == null || end == null) {
                return;
            }

            long currentTime = MinecraftClient.getInstance().world.getTime();
            long ticksElapsed = currentTime - RailgunClientState.startTick;
            long duration = RailgunClientState.resultDurationTicks;

            if (ticksElapsed < 0) {
                RailgunClientState.reset();
                return;
            }
            if (ticksElapsed >= duration) {
                RailgunClientState.reset();
                return;
            }

            float fadeRatio = 1f - ticksElapsed / (float) duration;
            fadeRatio = Math.max(fadeRatio, 0f);

            float red, green, blue, alpha;

            if (hit) {
                // Green fading beam on hit
                red = 0f;
                green = 1f;
                blue = 0f;
            } else {
                // Red fading beam on miss
                red = 1f;
                green = 0f;
                blue = 0f;
            }
            alpha = 0.85f * fadeRatio;
            float beamThickness = 0.1f;

            renderBeamBox(matrices, vertexConsumers, start, end, red, green, blue, alpha, beamThickness);
        }
        else {
        }
    }

    // Helper method to render the beam box with given parameters
    public static void renderBeamBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                     Vec3d start, Vec3d end,
                                     float red, float green, float blue, float alpha,
                                     float beamThickness) {

        Vec3d direction = end.subtract(start);
        Vec3d normDir = direction.normalize();

        Vec3d up = new Vec3d(0, 1, 0);
        Vec3d right = normDir.crossProduct(up);
        if (right.lengthSquared() < 1e-6) {
            up = new Vec3d(1, 0, 0);
            right = normDir.crossProduct(up);
        }
        right = right.normalize().multiply(beamThickness);
        Vec3d upAdjusted = right.crossProduct(normDir).normalize().multiply(beamThickness);

        Vec3d p1 = start.subtract(right).add(upAdjusted);
        Vec3d p2 = start.subtract(right).subtract(upAdjusted);
        Vec3d p3 = start.add(right).subtract(upAdjusted);
        Vec3d p4 = start.add(right).add(upAdjusted);

        Vec3d p5 = end.subtract(right).add(upAdjusted);
        Vec3d p6 = end.subtract(right).subtract(upAdjusted);
        Vec3d p7 = end.add(right).subtract(upAdjusted);
        Vec3d p8 = end.add(right).add(upAdjusted);

        Vec3d cameraPos = MinecraftClient.getInstance().player.getCameraPosVec(1);

        Vec3d rp1 = p1.subtract(cameraPos);
        Vec3d rp2 = p2.subtract(cameraPos);
        Vec3d rp3 = p3.subtract(cameraPos);
        Vec3d rp4 = p4.subtract(cameraPos);
        Vec3d rp5 = p5.subtract(cameraPos);
        Vec3d rp6 = p6.subtract(cameraPos);
        Vec3d rp7 = p7.subtract(cameraPos);
        Vec3d rp8 = p8.subtract(cameraPos);

        renderBox(
                vertexConsumers,
                matrices.peek().getPositionMatrix(),
                White,
                rp1, rp2, rp3, rp4,
                rp5, rp6, rp7, rp8,
                red, green, blue, alpha
        );
    }
}
