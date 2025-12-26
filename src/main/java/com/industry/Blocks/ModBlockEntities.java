package com.industry.Blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModBlockEntities {
    public static BlockEntityType<BeaconBeamTrapBlockEntity> BEACON_BEAM_TRAP;

    public static void registerModBlockEntities() {
        // force
        BEACON_BEAM_TRAP =
                Registry.register(
                        Registries.BLOCK_ENTITY_TYPE,
                        Identifier.of("industry", "beacon_beam_trap"),
                        BlockEntityType.Builder
                                .create(BeaconBeamTrapBlockEntity::new, ModBlocks.BEACON_BEAM_TRAP)
                                .build(null)
                );
    }
}