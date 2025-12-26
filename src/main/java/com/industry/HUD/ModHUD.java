package com.industry.HUD;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class ModHUD {

    private static boolean enabled = false;
    private static boolean registered = false;

    public static void start() {
        enabled = true;

        if (!registered) {
            HudRenderCallback.EVENT.register(ModHUD::render);
            registered = true;
        }
    }

    public static void stop() {
        enabled = false;
    }

    public static void toggle() {
        enabled = !enabled;
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        ArmourBackground.render(context, client);
        Armour.render(context, client);
        SelectedItems.render(context, client);
    }
}
