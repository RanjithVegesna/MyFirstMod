package com.industry.Commands;

import com.industry.Mod;
import com.industry.math.Cuboid;
import com.industry.math.Matrix3;
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
import static com.industry.math.Vector3.from;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class Ellipse {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("ellipse")
                            .then(argument("thickness", DoubleArgumentType.doubleArg(0.9999))
                                    .then(argument("blockState1", BlockStateArgumentType.blockState(registryAccess))
                                            .executes(Ellipse::run)
                                            .then(argument("blockState2", BlockStateArgumentType.blockState(registryAccess))
                                                    .executes(Ellipse::run)
                                                    .then(argument("blockState3", BlockStateArgumentType.blockState(registryAccess))
                                                            .executes(Ellipse::run)
                                                            .then(argument("blockState4", BlockStateArgumentType.blockState(registryAccess))
                                                                    .executes(Ellipse::run)
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
        BlockPos pos1 = (BlockPos) Mod.playerDataTable.query("Pos1", "UUID", playerId);
        BlockPos pos2 = (BlockPos) Mod.playerDataTable.query("Pos2", "UUID", playerId);

        Vector3 myClassPos1 = from(pos1);
        Vector3 myClassPos2 = from(pos2);

        Cuboid cuboid = new Cuboid(from(pos1), from(pos2));
        Vector3 center = cuboid.center;
        Matrix3 scalingMatrix = Matrix3.getScaleMatrix(cuboid.lenX, cuboid.lenY, cuboid.lenZ);

        Vector3 deepPos1 = myClassPos1.subtract(center);
        Vector3 deepPos2 = myClassPos2.subtract(center);

        double resolution = 0.05;
        List<Vector3> pointsOnCornerOfCube = new ArrayList<>();
        List<Vector3> pointsOnSurfaceOfSphere = new ArrayList<>();
        List<Vector3> pointsOnSurfaceOfEllipse = new ArrayList<>();
        List<Vector3> pointBeforeTranslating = new ArrayList<>();

        List<BlockState> blockStates = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            try {
                blockStates.add(BlockStateArgumentType.getBlockState(context, "blockState" + i).getBlockState());
            } catch (IllegalArgumentException ignored) {
                break;
            }
        }

        for (double x = -cuboid.halfLenX; x <= cuboid.halfLenX; x += resolution) {
            for (double y = -cuboid.halfLenY; y <= cuboid.halfLenY; y += resolution) {
                // +Z and -Z faces
                pointsOnCornerOfCube.add(new Vector3(x, y,  cuboid.halfLenZ));
                pointsOnCornerOfCube.add(new Vector3(x, y, -cuboid.halfLenZ));
            }
        }

        for (double x = -cuboid.halfLenX; x <= cuboid.halfLenX; x += resolution) {
            for (double z = -cuboid.halfLenZ; z <= cuboid.halfLenZ; z += resolution) {
                // +Y and -Y faces
                pointsOnCornerOfCube.add(new Vector3(x,  cuboid.halfLenY, z));
                pointsOnCornerOfCube.add(new Vector3(x, -cuboid.halfLenY, z));
            }
        }

        for (double y = -cuboid.halfLenY; y <= cuboid.halfLenY; y += resolution) {
            for (double z = -cuboid.halfLenZ; z <= cuboid.halfLenZ; z += resolution) {
                // +X and -X faces
                pointsOnCornerOfCube.add(new Vector3( cuboid.halfLenX, y, z));
                pointsOnCornerOfCube.add(new Vector3(-cuboid.halfLenX, y, z));
            }
        }


        for (Vector3 vector : pointsOnCornerOfCube) {
            Vector3 direction = vector.normalize();
            pointsOnSurfaceOfSphere.add(vector.normalize());
        }

        for (Vector3 vector : pointsOnSurfaceOfSphere) {
            pointsOnSurfaceOfEllipse.add(scalingMatrix.transform(vector));
        }

        for (Vector3 vector : pointsOnSurfaceOfEllipse) {
            BlockPos whereBlockShouldBePlaced = new BlockPos(BlockPos.ofFloored(vector.add(center).toVec3d()));
            BlockState state = blockStates.get(random.nextInt(blockStates.size()));
            world.setBlockState(whereBlockShouldBePlaced, state, 3);
        }

        return 1;
    }
}
