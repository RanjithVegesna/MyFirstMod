package com.industry.item;

import classes.Table;
import com.industry.Mod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

import static com.industry.Mod.playerDataTable;

public class WorldEditAxe extends Item {
    public WorldEditAxe(Settings settings) {
        super(settings);
    }

    public static BlockPos NONE =  new BlockPos(0, 67676767, 0);

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS; // or PASS
        }

        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        UUID playerId = player.getUuid();
        Table userTable = playerDataTable;
        if (userTable == null) return ActionResult.PASS;

        BlockPos position1 = (BlockPos) userTable.query("Pos1", "UUID", playerId);
        BlockPos position2 = (BlockPos) userTable.query("Pos2", "UUID", playerId);

        BlockPos clickedPos = context.getBlockPos();

        System.out.println("[DEBUG] useOnBlock triggered for " + player.getName().getString() + " at " + context.getBlockPos());

        if (position1.equals(NONE)) {
            userTable.update(userTable.getIndex("UUID", playerId), "Pos1", clickedPos);
            player.sendMessage(Text.literal("Position 1 Set.").formatted(Formatting.YELLOW));
            return ActionResult.success(true);
        }

        else if (position2.equals(NONE)) {
            userTable.update(userTable.getIndex("UUID", playerId), "Pos2", clickedPos);
            player.sendMessage(Text.literal("Position 2 Set.").formatted(Formatting.YELLOW));
            Mod.LOGGER.info(userTable.toString());
            return ActionResult.success(true);
        }

        // If both positions are set, reset
        userTable.update(userTable.getIndex("UUID", playerId), "Pos1", clickedPos);
        userTable.update(userTable.getIndex("UUID", playerId), "Pos2", NONE);
        player.sendMessage(Text.literal("Positions reset. Position 1 Set.").formatted(Formatting.YELLOW));
        return ActionResult.success(true);
    }

}
