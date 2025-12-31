package com.industry.serverSideImplementers.implementers;

import com.industry.item.amulet.RegenerationAmulet;
import com.industry.item.amulet.ResistanceAmulet;
import com.industry.item.amulet.SpeedAmulet;
import com.industry.item.amulet.StrengthAmulet;
import net.minecraft.entity.player.PlayerEntity;

public class AmuletImplementer {
    public static void implementAmulets(PlayerEntity player) {
        RegenerationAmulet.implementAmulet(player);
        ResistanceAmulet.implementAmulet(player);
        SpeedAmulet.implementAmulet(player);
        StrengthAmulet.implementAmulet(player);
    }
}
