package com.industry.Commands;

import com.industry.Mod;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

import static com.industry.Commands.CommandManager.isValid;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Box {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("room")
                            .then(argument("blockState1", BlockStateArgumentType.blockState(registryAccess))
                                    .executes(Box::run)
                                    .then(argument("blockState2", BlockStateArgumentType.blockState(registryAccess))
                                            .executes(Box::run)
                                            .then(argument("blockState3", BlockStateArgumentType.blockState(registryAccess))
                                                    .executes(Box::run)
                                                    .then(argument("blockState4", BlockStateArgumentType.blockState(registryAccess))
                                                            .executes(Box::run)
                                                    )
                                            )
                                    )
                            )
            );
        });
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if(!isValid(context)) return 0;

        Random random = new Random();
        PlayerEntity player = context.getSource().getPlayer();
        World world = context.getSource().getWorld();
        if (player == null || world == null) return 0;

        UUID playerId = player.getUuid();
        BlockPos pos1 = (BlockPos) Mod.playerDataTable.query("Pos1", "UUID", playerId);
        BlockPos pos2 = (BlockPos) Mod.playerDataTable.query("Pos2", "UUID", playerId);

        if (pos1 == null || pos2 == null) {
            context.getSource().sendError(Text.literal("You must set both positions first."));
            return 0;
        }

        List<BlockState> blockStates = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            try {
                blockStates.add(BlockStateArgumentType.getBlockState(context, "blockState" + i).getBlockState());
            } catch (IllegalArgumentException ignored) {
                break;
            }
        }

        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        mutable.set(x, y, z);
                        BlockState state = blockStates.get(random.nextInt(blockStates.size()));
                        world.setBlockState(mutable, state, 3);
                    }
                }
            }
        }

        return 1;
    }
}
