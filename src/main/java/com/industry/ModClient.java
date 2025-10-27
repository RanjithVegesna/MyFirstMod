package com.industry;

import com.industry.item.RailgunClientState;
import com.industry.packets.OrbitalLazerCannonPayload;
import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class ModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register codec on client side as well before registering receiver
        WorldRenderEvents.END.register(context -> {
            Render.renderBeam(context.matrixStack(), context.consumers());
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            OrbitalLazerCannonRendering.renderLazer(context.matrixStack(), context.consumers());
        });

        ClientPlayNetworking.registerGlobalReceiver(RailgunFlagsPayload.ID, (payload, context) -> {

            context.client().execute(() -> {
                boolean flag1 = payload.flag1;   // <- direct field access
                int flag2 = payload.flag2;
                Vec3d startPos = payload.start;
                Vec3d endPos = payload.end;
                MinecraftClient client = context.client();

                ItemStack itemStack = client.player.getMainHandStack();
                Item itemInHand = itemStack.getItem();
                if (flag1) {
                    RailgunClientState.triggerStart(startPos, endPos, 20);
                }
                if (flag2 == 1) {
                    RailgunClientState.triggerHit(startPos, endPos, 25, true);
                }
                if (flag2 == 0) {
                    RailgunClientState.triggerHit(startPos, endPos, 25, false);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(OrbitalLazerCannonPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Mod.LOGGER.info("Vector from packet {}", payload.vec);
                OrbitalLazerCannonRendering.triggerStart(payload.vec);
            });
        });
    }
}
