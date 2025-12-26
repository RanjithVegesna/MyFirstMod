package com.industry.MaterialsAndTools;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.TagKey;

public enum ModMaterials implements ToolMaterial {
    LifeSteal(2000, 8.0f, 15);

    private final int durability;
    private final float attackDamage;
    private final int enchantability;

    ModMaterials(int durability, float attackDamage, int enchantability) {
        this.durability = durability;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 1.0f; // default
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return null; // default for now
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
