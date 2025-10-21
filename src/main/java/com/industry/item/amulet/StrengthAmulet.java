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

import static com.industry.item.ModItems.STRENGTH_AMULET;

public class StrengthAmulet extends Item {
    public StrengthAmulet(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 10));
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
