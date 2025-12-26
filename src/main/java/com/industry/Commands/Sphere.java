package com.industry.Commands;

import com.industry.Mod;
import com.industry.math.Vector3;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.*;

import static com.industry.Commands.CommandManager.isValid;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class Sphere {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
            CommandManager.literal("sphere")
                .then(argument("thickness", DoubleArgumentType.doubleArg(0.9999))
                    .then(argument("blockState1", BlockStateArgumentType.blockState(registryAccess))
                        .executes(Sphere::run)
                        .then(argument("blockState2", BlockStateArgumentType.blockState(registryAccess))
                            .executes(Sphere::run)
                            .then(argument("blockState3", BlockStateArgumentType.blockState(registryAccess))
                                .executes(Sphere::run)
                                .then(argument("blockState4", BlockStateArgumentType.blockState(registryAccess))
                                        .executes(Sphere::run)
                                )
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
        BlockPos tempCenter = (BlockPos) Mod.playerDataTable.query("Pos1", "UUID", playerId);
        BlockPos tempPointOnCircumference = (BlockPos) Mod.playerDataTable.query("Pos2", "UUID", playerId);

        Vector3 center = new Vector3(tempCenter.getX(), tempCenter.getY(), tempCenter.getZ());
        Vector3 pointOnCircumference = new Vector3(tempPointOnCircumference.getX(), tempPointOnCircumference.getY(), tempPointOnCircumference.getZ());
        double distance = pointOnCircumference.distanceTo(center);
        double thickness = DoubleArgumentType.getDouble(context, "thickness");
        Vector3 origin = new Vector3(0, 0, 0);

        double resolution = 0.5;
        List<Vector3> pointsOnCornerOfCube = new ArrayList<>();
        List<Vector3> pointsOnSurfaceOfSphere = new ArrayList<>();
        List<Vector3> pointBeforeTranslating = new ArrayList<>();
        Vector3 cornerOfCube = new Vector3(distance, distance, distance);

        List<BlockState> blockStates = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            try {
                blockStates.add(BlockStateArgumentType.getBlockState(context, "blockState" + i).getBlockState());
            } catch (IllegalArgumentException ignored) {
                break;
            }
        }

        for (double x = -distance; x <= distance; x += resolution)
            for (double y = -distance; y <= distance; y += resolution) {
                pointsOnCornerOfCube.add(new Vector3(x, y,  distance));
                pointsOnCornerOfCube.add(new Vector3(x, y, -distance));
                pointsOnCornerOfCube.add(new Vector3(x,  distance, y));
                pointsOnCornerOfCube.add(new Vector3(x, -distance, y));
                pointsOnCornerOfCube.add(new Vector3( distance, x, y));
                pointsOnCornerOfCube.add(new Vector3(-distance, x, y));
            }

        for (Vector3 vector : pointsOnCornerOfCube) {
            Vector3 direction = vector.normalize();
            for (double i = distance - thickness / 2; i <= distance + thickness / 2; i += 0.5) {
                pointsOnSurfaceOfSphere.add(direction.multiply(i));
            }
        }

        for (Vector3 vector : pointsOnSurfaceOfSphere) {Vec3d tempVec = vector.toVec3d();
            BlockPos whereBlockShouldBePlaced = new BlockPos(BlockPos.ofFloored(vector.add(center).toVec3d()));
            BlockState state = blockStates.get(random.nextInt(blockStates.size()));
            world.setBlockState(whereBlockShouldBePlaced, state, 3);}

        return 1;
    }
}
