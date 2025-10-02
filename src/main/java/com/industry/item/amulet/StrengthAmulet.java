package com.industry.item.amulet;

import com.industry.item.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class StrengthAmulet extends Item {

    public static StrengthAmulet STRENGTH_AMULET = new StrengthAmulet(new Item.Settings());

    public StrengthAmulet(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 255));
            player.getItemCooldownManager().set(this, 600);
        }
        ItemUtils.spawnParticles(world, player, ParticleTypes.DRAGON_BREATH, 50);
        return super.use(world, player, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient && attacker instanceof PlayerEntity) {
            Box box = target.getBoundingBox().expand(20);
            for (LivingEntity entity : attacker.getWorld().getEntitiesByClass(LivingEntity.class, box, e -> !e.equals(attacker))){
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 2));
                ItemUtils.spawnParticles(target.getWorld(), target, ParticleTypes.DRAGON_BREATH, 100);
            }
        }
        return super.postHit(stack, target, attacker);
    }
    public static void implementAmulet(PlayerEntity player) {
        if (ItemUtils.isInEitherHand(player, STRENGTH_AMULET))
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 1));
        else if (ItemUtils.isInInventory(player, STRENGTH_AMULET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 0));
        }
    }
}
