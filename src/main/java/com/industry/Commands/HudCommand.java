package com.industry.Commands;

import com.industry.HUD.ModHUD;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class HudCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("hud")
                            .then(literal("on").executes(ctx -> {
                                ModHUD.start();
                                send("HUD enabled");
                                return 1;
                            }))
                            .then(literal("off").executes(ctx -> {
                                ModHUD.stop();
                                send("HUD disabled");
                                return 1;
                            }))
                            .then(literal("toggle").executes(ctx -> {
                                ModHUD.toggle();
                                send("HUD toggled");
                                return 1;
                            }))
            );
        });
    }

    private static void send(String msg) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(net.minecraft.text.Text.literal(msg), false);
        }
    }
}
