package com.industry.item;

import com.industry.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class Spectator extends Item {

    private static Map<LivingEntity, Long> playerMap = new HashMap<>();

    public Spectator(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        playerMap.put(user, world.getTime());
        if (!(user instanceof ServerPlayerEntity player)) {return new TypedActionResult<>(ActionResult.FAIL, user.getStackInHand(hand));}
        player.changeGameMode(GameMode.SPECTATOR);
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity user) {
            if (playerMap.get(user) == null) {return;}
            if (!(user instanceof ServerPlayerEntity player)) {return;}
            if (world.getTime() > playerMap.get(user) + 100) {
                playerMap.remove(user);
                player.changeGameMode(GameMode.DEFAULT);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
