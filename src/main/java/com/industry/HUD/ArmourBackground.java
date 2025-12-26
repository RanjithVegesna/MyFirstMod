package com.industry.HUD;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import static com.industry.textures.ModTextures.GUI_Icons;

public class ArmourBackground implements HUDObject{

    public static void render(DrawContext context, MinecraftClient client) {
        int width  = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        int hotbarX = width / 2 - 91;
        int hotbarY = height - 22;

        int sideX = hotbarX + 182 + 6;
        int sideY = hotbarY;

        int i = 0;
        assert client.player != null;
        for (ItemStack armour : client.player.getAllArmorItems()) {
            context.drawTexture(
                    GUI_Icons,
                    5,
                    height - 115 - i * 22,
                    0, 0,
                    22, 22,
                    22, 22
            );
            i++;
        }
        for (int j = 0; j < 2; j++) {
            context.drawTexture(
                    GUI_Icons,
                    5,
                    3 * height / 4 +  height / 100 + j * 22 - 22,
                    0, 0,
                    22, 22,
                    22, 22
            );

        }
    }
}
