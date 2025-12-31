package com.industry.entities;

import com.industry.Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static EntityType<DeadBodyEntity> DEAD_BODY =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(Mod.MOD_ID, "dead_body"),
                    EntityType.Builder.create(DeadBodyEntity::new, SpawnGroup.MISC)
                            .dimensions(0.6f, 0.6f).
                            maxTrackingRange(1).build()
            );
}
