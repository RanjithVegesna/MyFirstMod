package com.industry.Rendering.GravitonLauncher;

import com.industry.Rendering.OrbitalLaserCannon.Beam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaterniond;

public class GravitonLauncherRendering {
    public Quaterniond rotation = new Quaterniond(0, 0, 0, 1);
    public Vec3d center = new Vec3d(0, 0, 0);
    public boolean isActive = false;
    public States currentState = States.NONE;
    public States previousState
    public long startTick = 0;

    private double x, y, z , w;
    private double deltaX, deltaY, deltaZ, deltaW;

    public void triggerStart(Vec3d center) {
        this.center = center;
        isActive = true;
        currentState = States.CHARGE;
    }

    public void render() {
        if (!isActive) return;
        if (currentState == States.NONE) return;

        assert MinecraftClient.getInstance().world != null;
        long elapsed = MinecraftClient.getInstance().world.getTime() - startTick;

        if (elapsed > 0) currentState = States.CHARGE;
        if (elapsed > 20) currentState = States.MAIN;
        if (elapsed > 200) currentState = States.NONE;



    }
}
