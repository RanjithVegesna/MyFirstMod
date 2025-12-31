package com.industry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

    public static SoundEvent DEATH_SOUND = register("death.sound");

    private static SoundEvent register(String id) {
        Identifier identifier = Identifier.of(Mod.MOD_ID, id);
        return Registry.register(
                Registries.SOUND_EVENT,
                identifier,
                SoundEvent.of(identifier)
        );
    }

    public static void register() {

    }
}
