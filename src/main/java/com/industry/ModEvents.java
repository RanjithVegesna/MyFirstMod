package com.industry;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.industry.item.ModItems.AURA;

public class ModEvents {
    public static void register(){
        ServerLivingEntityEvents.ALLOW_DEATH.register(ModEvents::onAllowDeath);
    }

    public static boolean onAllowDeath(LivingEntity entity, DamageSource source, float amount) {
        if (!findTotem(entity).isOf(AURA)) return true;

        findTotem(entity).decrement(0);

        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 2));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 4));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 0));
        entity.setHealth(2.0F);

        entity.getWorld().sendEntityStatus(entity, (byte) 35);

        return false;
    }

    public static ItemStack findTotem(LivingEntity living) {
        if (living.getMainHandStack().isOf(AURA))
            return living.getMainHandStack();
        if (living.getOffHandStack().isOf(AURA))
            return living.getOffHandStack();

        return ItemStack.EMPTY;
    }
}
