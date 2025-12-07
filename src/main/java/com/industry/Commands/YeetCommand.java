package com.industry.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;

import static net.minecraft.server.command.CommandManager.*;

public class YeetCommand {
    public static void register() {
        LiteralArgumentBuilder<ServerCommandSource> cmd = literal("yeet")
                .then(argument("target", EntityArgumentType.entities())
                        .executes(YeetCommand::run));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(cmd);
        });
    }

    static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgumentType.getEntities(context, "target");
        for (Entity entity : entities) {
            Vec3d velocity = new Vec3d(0, 5, 0); // launch upwards
            entity.setVelocity(velocity);
            entity.velocityModified = true;
        }
        context.getSource().sendFeedback(() -> Text.literal("YEEEET!"), true);
        return 1;
    }
}
