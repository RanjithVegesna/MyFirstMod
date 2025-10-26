package com.industry.item;

import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.industry.item.ItemUtils.rayCastEntities;

public class Railgun extends Item {

    public enum Outcomes {
        HIT(1), MISS(0), NONE(-1);

        private int i;

        Outcomes(int i) {
            this.i = i;
        }

        public int getValue() {
            return i;
        }
    }

    public static int maxDistance = 400;
    public static long lastTimeHit;
    public static int delay = 20;
    public static int fadeOutDelay = 25;
    public static Outcomes hitEntityResult = Outcomes.NONE;

    public Railgun(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            if (serverPlayer.isSneaking()) {
                Box box = serverPlayer.getBoundingBox().expand(200);
                for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, entity -> entity != user)) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600, 0));
                    serverPlayer.getItemCooldownManager().set(this, 100);
                }
                return TypedActionResult.success(user.getStackInHand(hand));
            }
            serverPlayer.getItemCooldownManager().set(this, 50);
            lastTimeHit = world.getTime();

            CustomPayload startPacket = new RailgunFlagsPayload(true, -1, Vec3d.ZERO, Vec3d.ZERO);
            ServerPlayNetworking.send(serverPlayer, startPacket);

            for (ServerPlayerEntity target : PlayerLookup.tracking(serverPlayer)) {
                ServerPlayNetworking.send(target, startPacket);
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof ServerPlayerEntity player) {
            if (lastTimeHit + delay == world.getTime()) {

                // Beam direction and hand offset logic (same as client)
                Vec3d lookVec = player.getRotationVec(1.0F).normalize();
                Vec3d handOffset = lookVec.multiply(0.9).add(0, -0.25, +0.25);
                Vec3d start =  player.getHandPosOffset(player.getMainHandStack().getItem()).add(player.getPos()).add(player.getRotationVec(1.0F).normalize()).add(new Vec3d(0, 1.5, 0));
                Vec3d end = start.add(lookVec.multiply(maxDistance));

                List<EntityHitResult> hits = rayCastEntities(player, maxDistance);

                if (!hits.isEmpty()) {
                    hitEntityResult = Outcomes.HIT;
                }
                else {
                    hitEntityResult = Outcomes.MISS;
                }

                for (EntityHitResult hit : hits) {
                    Entity target = hit.getEntity();
                    if (target instanceof LivingEntity living) {
                        world.sendEntityStatus(living, (byte)60); // death particles
                        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                        if (lightning != null) {
                            lightning.setCosmetic(true);
                            lightning.refreshPositionAfterTeleport(Vec3d.of(living.getBlockPos()));
                            world.spawnEntity(lightning);
                        }
                        living.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 10.0f, 0.5f);
                        living.discard();
                    }
                }


                // Send result to player and all tracking players
                CustomPayload packet = new RailgunFlagsPayload(false, hitEntityResult.getValue(), start, end);
                ServerPlayNetworking.send(player, packet);

                for (ServerPlayerEntity target : PlayerLookup.tracking(player)) {
                    ServerPlayNetworking.send(target, packet);
                }
            }
        }
    }
}
