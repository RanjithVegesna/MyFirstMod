package com.industry.Commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Up {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("up")
                            .then(argument("amount", IntegerArgumentType.integer())
                            .executes(Up::run))
            );
        });
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        int amount = IntegerArgumentType.getInteger(context, "amount");

        assert player != null;
        player.teleport(
                context.getSource().getWorld(),
                player.getX(),
                player.getY() + amount,
                player.getZ(),
                EnumSet.noneOf(PositionFlag.class),
                player.getYaw(),
                player.getPitch()
        );
        context.getSource().getWorld().setBlockState(player.getBlockPos().subtract(new BlockPos(0 , 1, 0)), Blocks.GLASS.getDefaultState());

        return 1;
    }
}
