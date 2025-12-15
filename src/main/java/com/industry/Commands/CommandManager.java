package com.industry.Commands;

import classes.Table;
import com.industry.Mod;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class CommandManager {
     static boolean isValid(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        UUID uuid = player.getUuid();

        Table table = Mod.playerDataTable;
        int row = table.getIndex("UUID", uuid);

        if (row == -1) {
            player.sendMessage(
                    Text.literal("No selection found.")
                            .formatted(Formatting.RED),
                    false
            );
            return false;
        }

        BlockPos pos1 = (BlockPos) table.query("Pos1", "UUID", uuid);
        BlockPos pos2 = (BlockPos) table.query("Pos2", "UUID", uuid);

        if (pos1 == null || pos2 == null) {
            player.sendMessage(
                    Text.literal("Incomplete selection.")
                            .formatted(Formatting.RED),
                    false
            );
            return false;
        }
        return true;
    }
}
