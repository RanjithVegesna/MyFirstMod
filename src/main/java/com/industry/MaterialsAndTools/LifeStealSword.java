package com.industry.MaterialsAndTools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;

import java.util.Objects;

import static com.industry.MaterialsAndTools.ModMaterials.LifeSteal;

public class LifeStealSword extends SwordItem {

    static long modifierNumber = 0;

    public LifeStealSword(Settings settings) {
        super(LifeSteal, settings.attributeModifiers(
                SwordItem.createAttributeModifiers(
                        LifeSteal,
                        10,
                        0
                )));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        EntityAttributeModifier modifierToEnemy = new EntityAttributeModifier(
                Identifier.of("life_steal_modifier_enemy" + modifierNumber++),
                -2,
                EntityAttributeModifier.Operation.ADD_VALUE
        );

        Objects.requireNonNull(target.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).addTemporaryModifier(modifierToEnemy);

        EntityAttributeModifier modifierToAttacker = new EntityAttributeModifier(
                Identifier.of("life_steal_modifier_attacker" + modifierNumber++),
                +2,
                EntityAttributeModifier.Operation.ADD_VALUE
        );

        Objects.requireNonNull(attacker.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).addTemporaryModifier(modifierToAttacker);
        attacker.setHealth(attacker.getHealth() + 2);

        return false;
    }
}
