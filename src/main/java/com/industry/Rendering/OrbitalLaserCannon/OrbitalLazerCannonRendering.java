package com.industry.Rendering.OrbitalLaserCannon;

import com.industry.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import com.industry.Rendering.OrbitalLaserCannon.Beam.State;

import java.util.ArrayList;
import java.util.List;

public class OrbitalLazerCannonRendering {

    private static State previousState = State.NONE;
    private static long start = 0;
    private static State currentState = State.NONE;
    private static Vec3d center = Vec3d.ZERO;
    private static long startTick = 0;
    private static final List<Beam> beams = new ArrayList<>();

    public static void triggerStart(Vec3d vector) {
        center = vector;
        currentState = State.BEAM;
        startTick = MinecraftClient.getInstance().world.getTime();
        beams.clear();
        System.out.println("[Orbital] Triggered start at: " + vector);
    }

    public static void renderLazer(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        long elapsedTime = client.world.getTime() - startTick;

        if (elapsedTime > 0) currentState = State.BEAM;
        if (elapsedTime > 40) currentState = State.SPLITTING;
        if (elapsedTime > 100) currentState = State.AFTER_SPLIT;
        if (elapsedTime > 140) currentState = State.NONE;

        if (currentState == State.NONE) return;

        // Camera-relative position
        Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
        Vec3d relCenter = center;

        if (currentState == State.BEAM) {
            if (previousState != State.BEAM) {
                start = client.world.getTime();
            }
            long elapsed = client.world.getTime() - start;
            Mod.LOGGER.info("Elapsed time: {}", elapsed);
            double size = elapsed / 100.0;
            Beam beam = new Beam(relCenter, cameraPos);
            beam.renderBeam(vertexConsumers, matrices, size);
        }

        if (currentState == State.SPLITTING) {
            double speed = 0.1;
            double sqrt = Math.sqrt(speed + speed);
            if (previousState != State.SPLITTING) {
                double[] angles = {0, Math.PI/4, Math.PI/2, 3*Math.PI/4, Math.PI, 5*Math.PI/4, 3*Math.PI/2, 7*Math.PI/4};

                for (double angle : angles) {
                    double vx = speed * Math.cos(angle);
                    double vz = speed * Math.sin(angle);
                    beams.add(new Beam(relCenter, new Vec3d(vx, 0, vz)));
                }

            }

            for (Beam beam : beams) {
                beam.renderBeam(vertexConsumers, matrices, 1);
                beam.moveBeam();
            }
        }
        if (currentState == State.AFTER_SPLIT) {
            for (Beam beam : beams) {
                beam.renderBeam(vertexConsumers, matrices, 1);
            }
        }

        previousState = currentState;
    }
}