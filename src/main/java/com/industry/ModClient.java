package com.industry;

import com.industry.item.RailgunClientState;
import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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

        ClientPlayNetworking.registerGlobalReceiver(RailgunFlagsPayload.ID, (payload, context) -> {
            boolean flag1 = payload.flag1();
            int flag2 = payload.flag2();

            context.client().execute(() -> {
                MinecraftClient client = context.client();

                ItemStack itemStack = client.player.getMainHandStack();
                Item itemInHand = itemStack.getItem();
                Vec3d handPosOffset = client.player.getHandPosOffset(itemInHand);  // whichever item your player is holding
                Vec3d lookVec = client.player.getRotationVec(1.0f).normalize();

                Vec3d startPos = client.player.getCameraPosVec(1).add(handPosOffset).add(lookVec.multiply(0.9).add(0, -0.25, -0.25));
                Vec3d endPos   = startPos.add(lookVec.multiply(client.options.getViewDistance().getValue() * 16));
                Mod.LOGGER.info("In Log");

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
    }
}
