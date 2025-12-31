package com.industry.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.List;

public class DeadBodyEntity extends LivingEntity {

    private int lifeTicks = 20 * 10;

    public DeadBodyEntity(EntityType<? extends DeadBodyEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public void tick() {
        if (getEntityWorld().isClient) return;
        lifeTicks--;
        System.out.println(lifeTicks);
        if (lifeTicks < -200) this.kill();
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        lifeTicks = nbt.getInt("deadBody");
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("deadBody", lifeTicks);
    }


}
