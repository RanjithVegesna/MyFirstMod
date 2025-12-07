package com.industry.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Gather {
    public static void register() {
        LiteralArgumentBuilder<ServerCommandSource> cmd = literal("gather")
                        .executes(Gather::run);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(cmd);
        });
    }

    public static Box getSimulationBox(
            ServerWorld world, Vec3d center) {
        int simDist = 16; // how many chunks
        int radius = simDist * 16; // convert to blocks

        return new Box(
                center.x - radius, center.y - 500, center.z - radius,  // min box
                center.x + radius, center.y + 500, center.z + radius   // max box
        );
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        World world = context.getSource().getWorld();
        Vec3d center = context.getSource().getPosition();
        for (Entity entity : world.getEntitiesByClass(Entity.class, getSimulationBox(context.getSource().getWorld(), context.getSource().getPosition()), entity -> true )){
            entity.teleport(context.getSource().getWorld(), center.x, center.y, center.x, null, 0, 0);
        }
        return 1;
    }
}
