package com.industry.item.amulet;

import com.industry.item.ItemUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ResistanceAmulet extends Item {

    public static ResistanceAmulet RESISTANCE_AMULET = new ResistanceAmulet(new Settings());

    public ResistanceAmulet(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 255));
            player.getItemCooldownManager().set(this, 600);
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.setCosmetic(true); // 💡 disables fire and other world effects
                lightning.refreshPositionAfterTeleport(Vec3d.of(player.getBlockPos()));
                world.spawnEntity(lightning);
            }
        }
        ItemUtils.spawnParticles(world, player, ParticleTypes.ELECTRIC_SPARK, 50);
        return super.use(world, player, hand);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient && attacker instanceof PlayerEntity) {
            Box box = target.getBoundingBox().expand(20);
            for (LivingEntity ignored : attacker.getWorld().getEntitiesByClass(LivingEntity.class, box, e -> !e.equals(attacker))) {
                target.setHealth(target.getHealth() / 2);
                ItemUtils.spawnParticles(target.getWorld(), target, ParticleTypes.ASH, 50);
            }

        }
        return super.postHit(stack, target, attacker);
    }

    public static void implementAmulet(PlayerEntity player) {
        if (ItemUtils.isInEitherHand(player, RESISTANCE_AMULET))
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1));
        else if (ItemUtils.isInInventory(player, RESISTANCE_AMULET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 0));
        }
    }
}
