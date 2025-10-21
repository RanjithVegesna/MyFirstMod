package com.industry.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class RailgunClientState {

    public enum BeamPhase {
        CHARGING,
        RESULT,
        NONE
    }

    private static boolean beamActive = false;
    private static BeamPhase phase = BeamPhase.NONE;

    private static Vec3d startPos;
    private static Vec3d endPos;
    private static boolean didHit;

    public static long startTick; // Use Minecraft world ticks
    public static long resultDurationTicks; // Duration in ticks

    // ─────────────────────────────────────────────
    // 🔹 Trigger the START of the beam (charging phase)
    public static void triggerStart(Vec3d start, Vec3d end, long durationTicks) {
        beamActive = true;
        phase = BeamPhase.CHARGING;
        startPos = start;
        endPos = end;
        startTick = getCurrentTick();
        resultDurationTicks = durationTicks;
    }

    // ─────────────────────────────────────────────
    // 🔹 Trigger the HIT or MISS (result phase)
    public static void triggerHit(Vec3d start, Vec3d end, long fadeDurationTicks, boolean hit) {
        beamActive = true;
        phase = BeamPhase.RESULT;
        startPos = start;
        endPos = end;
        didHit = hit;
        startTick = getCurrentTick();
        resultDurationTicks = fadeDurationTicks;
    }

    // ─────────────────────────────────────────────
    // 🔹 Reset everything (fade-out complete)
    public static void reset() {
        beamActive = false;
        phase = BeamPhase.NONE;
        startPos = null;
        endPos = null;
        didHit = false;
        startTick = 0;
        resultDurationTicks = 0;
    }

    // ─────────────────────────────────────────────
    // 🔹 Accessors
    public static boolean isBeamActive() {
        return beamActive;
    }

    public static BeamPhase getPhase() {
        return phase;
    }

    public static Vec3d getStartPos() {
        return startPos;
    }

    public static Vec3d getEndPos() {
        return endPos;
    }

    public static boolean didHit() {
        return didHit;
    }

    public static float getChargeProgress() {
        if (phase != BeamPhase.CHARGING || resultDurationTicks <= 0) return 0f;
        long elapsedTicks = getCurrentTick() - startTick;
        return Math.min(elapsedTicks / (float) resultDurationTicks, 1f);
    }

    private static long getCurrentTick() {
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().world != null) {
            return MinecraftClient.getInstance().world.getTime();
        }
        return 0;
    }
}
