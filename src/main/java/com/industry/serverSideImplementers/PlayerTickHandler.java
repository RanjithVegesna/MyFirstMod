package com.industry.serverSideImplementers;

import com.industry.serverSideImplementers.implementers.AmuletImplementer;
import com.industry.serverSideImplementers.implementers.LauncherLowFrictionImplementer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

public class PlayerTickHandler {
    public static void tick(PlayerEntity player) {
        removeCooldowns(player);
        AmuletImplementer.implementAmulets(player);
        LauncherLowFrictionImplementer.implement(player);
    }

    private static void removeCooldowns(PlayerEntity player) {
        player.getItemCooldownManager().remove(Items.WIND_CHARGE);
    }
}
