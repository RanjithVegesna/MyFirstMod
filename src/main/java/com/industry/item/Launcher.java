package com.industry.item;

import com.industry.Mod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashSet;

public class Launcher extends Item {
    public static HashSet<PlayerEntity> lowFrictionPlayers = new HashSet<>();

    public Launcher(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> returnValue = super.use(world,  user, hand);

        if (world.isClient()) return returnValue;

        Mod.LOGGER.info("Velocity before launch = {}", user.getVelocity());

        user.getItemCooldownManager().set(this, 200);
        user.setVelocity(user.getVelocity().add(user.getRotationVec(1).multiply(10)));

        user.velocityModified = true;
        lowFrictionPlayers.add(user);

        Mod.LOGGER.info("Velocity after launch = {}", user.getVelocity());

        return returnValue;
    }
}
