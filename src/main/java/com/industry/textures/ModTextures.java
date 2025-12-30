package com.industry.textures;

import com.industry.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ModTextures {
    public static final Identifier Red = Identifier.of(Mod.MOD_ID, "textures/render/red.png");
    public static final Identifier Green = Identifier.of(Mod.MOD_ID, "textures/render/green.png");
    public static final Identifier Blue = Identifier.of(Mod.MOD_ID, "textures/render/light_blue.png");
    public static final Identifier White = Identifier.of(Mod.MOD_ID, "textures/render/white.png");
    public static final Identifier Graviton =  Identifier.of(Mod.MOD_ID, "textures/render/graviton.png");
    public static final Identifier GUI_Icons = Identifier.of("minecraft", "textures/gui/sprites/gamemode_switcher/slot.png");

    public static final Identifier BeamBlockFront = Identifier.of(Mod.MOD_ID, "textures/render/beam_block_front.png");
    public static final Identifier BeamBlockSide = Identifier.of(Mod.MOD_ID, "textures/render/beam_block_side.png");
    public static final Identifier BeamBlockBack = Identifier.of(Mod.MOD_ID, "textures/render/beam_block_back.png");

    public static void register() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getTextureManager() == null) {return;}

        client.getTextureManager().bindTexture(White);
    }
}
