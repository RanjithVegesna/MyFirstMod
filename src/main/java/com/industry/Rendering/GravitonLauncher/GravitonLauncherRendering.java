package com.industry.Rendering.GravitonLauncher;

import com.industry.Rendering.GravitonLauncher.States;
import com.industry.math.Vector3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Random;

public class GravitonLauncherRendering {
    boolean isActive;
    double x, y, z;
    double deltaX, deltaY, deltaZ;
    double theta = 1; // slow rotation
    long startTime;
    double size;

    Random random = new Random();

    Cube cube;
    States currentState;
    States previousState;

    Vector3 center;

    public void triggerStart(Vector3 vector) {
        isActive = true;
        center = vector;
        cube = null;
        previousState = States.NONE;
        currentState = States.CHARGE;
        startTime = MinecraftClient.getInstance().world.getTime();
    }

    public void render(VertexConsumerProvider vertexConsumer, MatrixStack matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        long elapsed = client.world.getTime() - startTime;

        // Phase selection
        if (elapsed > 220) currentState = States.NONE;
        else if (elapsed > 40) currentState = States.MAIN;
        else currentState = States.CHARGE;

        if (!isActive || currentState == States.NONE) return;

        // Initialize cube if not already done
        if (cube == null) {
            cube = new Cube(center, 0.5);
            x = random.nextInt(0, 90);
            y = random.nextInt(0, 90);
            z = random.nextInt(0, 90);
        }

        // CHARGE phase: grow cube smoothly
        if (currentState == States.CHARGE) {
            size = 0.5 + ((double) elapsed / 50.0); // slower growth
            cube.setSize(size);
        }

        // MAIN phase: subtle pulsing
        if (currentState == States.MAIN) {
            cube.setSize(size + 0.2 * Math.cos((double) elapsed / 5.0));
        }

        // Smooth rotation updates
        if (elapsed % 10 == 0) {
            deltaX = random.nextDouble(-2, 2);
            deltaY = random.nextDouble(-2, 2);
            deltaZ = random.nextDouble(-2, 2);
        } else {
            x += deltaX;
            y += deltaY;
            z += deltaZ;
        }

        cube.rotate(new Vector3(x, y, z), theta, 0);
        cube.render(vertexConsumer, matrixStack, 1, 1, 1, 1);
    }
}
