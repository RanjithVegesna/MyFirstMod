package com.industry.MaterialsAndTools;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public enum ModMaterials implements ToolMaterial {
    LifeSteal(2000, 8.0f, 15, 100);

    private final int durability;
    private final float attackDamage;
    private final int enchantability;
    private final float miningSpeed;

    ModMaterials(int durability, float attackDamage, int enchantability, float miningSpeed) {
        this.durability = durability;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.miningSpeed = miningSpeed;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return miningSpeed; // default
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of("minecraft", "stone")); // or some safe default

    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.DIAMOND);
    }
}
