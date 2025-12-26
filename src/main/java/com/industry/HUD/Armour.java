package com.industry.HUD;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.net.Inet4Address;

import static com.industry.textures.ModTextures.GUI_Icons;

public class Armour {
    public static void render(DrawContext context, MinecraftClient client) {
        int width  = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        int hotbarX = width / 2 - 91;
        int hotbarY = height - 22;

        int sideX = hotbarX + 182 + 6;
        int sideY = hotbarY;

        if (client.player == null || client.world == null)return;
        int i = 0;
        for (ItemStack armour : client.player.getAllArmorItems()) {
            context.drawItem(armour, 8,
                    height - 112 - i * 22);

        int fullDurability = armour.getMaxDamage();
        int remainingDurability = fullDurability - armour.getDamage();
        int damage = armour.getDamage();
        context.drawText(
                client.textRenderer,
                (Text) Text.literal(remainingDurability+ ""),
                30, height - 108 - i * 22,
                MathHelper.hsvToArgb((1 - (float) damage / fullDurability) / 3.0f, 0.7f, 0.8f, 1),
                false
        );

            i++;
        }
    }
}
