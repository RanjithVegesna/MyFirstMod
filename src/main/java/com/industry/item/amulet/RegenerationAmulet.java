package com.industry.item.amulet;

import com.industry.item.ItemUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import static com.industry.item.ModItems.REGENERATION_AMULET;

public class RegenerationAmulet extends Item {
    public RegenerationAmulet(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.setHealth(player.getMaxHealth());
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
        if (!target.getWorld().isClient && attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(this)) {
                Box box = target.getBoundingBox().expand(6);
                for (LivingEntity entity : attacker.getWorld().getEntitiesByClass(LivingEntity.class, box, e -> !e.equals(attacker))) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 300, 2));
                    ItemUtils.spawnParticles(attacker.getWorld(), attacker, ParticleTypes.ANGRY_VILLAGER, 50);
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }



    public static void implementAmulet(PlayerEntity player) {
        if (ItemUtils.isInEitherHand(player, REGENERATION_AMULET))
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 1));
        else if (ItemUtils.isInInventory(player, REGENERATION_AMULET)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0));
        }
    }
}
