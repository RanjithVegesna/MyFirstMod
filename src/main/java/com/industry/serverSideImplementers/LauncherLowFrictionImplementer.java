package com.industry.serverSideImplementers;

import com.industry.item.Launcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class LauncherLowFrictionImplementer {

    public static void implement(PlayerEntity player) {
        if (Launcher.lowFrictionPlayers.contains(player)) {
            Vec3d velocity = player.getVelocity();
            if (!player.isOnGround() && !player.isTouchingWater() && !player.isInLava()) {
                if (velocity.y > 0) {
                    player.setVelocity(velocity.multiply(1.05));
                    player.velocityModified = true;
                }
            } else {
                Launcher.lowFrictionPlayers.remove(player);
            }
        }
    }
}
