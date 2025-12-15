package com.industry.Commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

import java.util.EnumSet;

import static net.minecraft.server.command.CommandManager.literal;

public class Thru {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("thru").executes(Thru::run));
        });
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayerOrThrow();
        ServerWorld world = (ServerWorld) context.getSource().getWorld();

        Vec3d eyePos = player.getCameraPosVec(1);
        Vec3d lookDir = player.getRotationVec(1);

        double maxDistance = 100; // max raycast distance
        Vec3d endPos = eyePos.add(lookDir.multiply(maxDistance));

        // First raycast: detect the first solid block
        BlockHitResult hit = world.raycast(new RaycastContext(
                eyePos,
                endPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        Vec3d teleportPos;
        Vec3d firstContactPos = hit.getPos();

        if (hit.getType() == HitResult.Type.BLOCK) {
            // Second raycast: continue beyond the block by 1-2 blocks
            boolean flag = false;
            for (int i = 0; i < 90; i++) {
                firstContactPos = firstContactPos.add(lookDir.multiply(0.75));
                if (world.getBlockState(BlockPos.ofFloored(firstContactPos)).isAir()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                context.getSource().sendFeedback(() -> Text.literal("No space to phase through!"), true);
                return 0;
            };
            Vec3d beyond = firstContactPos.add(lookDir.multiply(1.0));

            // Find safe Y position
            BlockPos safePos = new BlockPos((int) beyond.x, (int) beyond.y, (int) beyond.z);
            if (!world.getBlockState(safePos).isAir()) {
                safePos = safePos.up();
            }

            teleportPos = new Vec3d(
                    safePos.getX() + 0.5,
                    safePos.getY(),
                    safePos.getZ() + 0.5
            );
        } else {
            // No block hit → teleport max distance
            teleportPos = endPos;
        }

        // Teleport player
        player.teleport(
                world,
                teleportPos.x,
                teleportPos.y,
                teleportPos.z,
                EnumSet.noneOf(PositionFlag.class),
                player.getYaw(),
                player.getPitch()
        );

        // Optional: place a glass block below the player
        BlockPos below = new BlockPos((int) teleportPos.x, (int) teleportPos.y, (int) teleportPos.z).down();
        world.setBlockState(below, Blocks.GLASS.getDefaultState());

        return 1;
    }
}
