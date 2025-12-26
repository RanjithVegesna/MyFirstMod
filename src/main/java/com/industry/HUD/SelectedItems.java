package com.industry.HUD;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class SelectedItems implements HUDObject {

    public static void render(DrawContext context, MinecraftClient client) {
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        int hotbarX = width / 2 - 91;
        int hotbarY = height - 22;

        int sideX = hotbarX + 182 + 6;
        int sideY = hotbarY;

        assert client.player != null;
        int i = 0;
        ItemStack mainHandStack = client.player.getMainHandStack();
        ItemStack offHandStack = client.player.getOffHandStack();

        int x = hotbarX - width / 7 - width / 8 - width / 200;
        context.drawItem(mainHandStack, x, 3 * height / 4 + height / 50 + 22 - 22);
        context.drawItem(offHandStack, x, 3 * height / 4 + height / 50 - 22);

        if (mainHandStack.isDamageable()) {

            int fullDurability = mainHandStack.getMaxDamage();
            int remainingDurability = fullDurability - mainHandStack.getDamage();
            int damage = mainHandStack.getDamage();

            context.drawText(
                    client.textRenderer,
                    (Text) Text.literal(remainingDurability + "" ),
                    30, height - 59,
                    MathHelper.hsvToArgb((1 - (float) damage / fullDurability) / 3.0f, 0.7f, 0.8f, 1),
                    false
            );

        }

        if (offHandStack.isDamageable()) {

            int fullDurability = offHandStack.getMaxDamage();
            int remainingDurability = fullDurability - offHandStack.getDamage();
            int damage = offHandStack.getDamage();

            context.drawText(
                    client.textRenderer,
                    (Text) Text.literal(remainingDurability + ""),
                    30, height - 81,
                    MathHelper.hsvToArgb((1 - (float) damage / fullDurability) / 3.0f, 0.7f, 0.8f, 255),
                    false
            );

        }
    }
}