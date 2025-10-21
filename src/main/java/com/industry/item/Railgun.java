package com.industry.item;

import com.industry.packets.RailgunFlagsPayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.industry.item.ItemUtils.rayCastEntities;
import static com.industry.item.MobSwitcherItem.rayCastEntity;

public class Railgun extends Item {

    public boolean clearBeam = false;

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

            CustomPayload startPacket = new RailgunFlagsPayload(true, -1);
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
                Vec3d start = player.getPos().add(0, player.getStandingEyeHeight(), 0).add(handOffset);
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
                        living.kill(); // Or apply damage
                    }
                }


                // Send result to player and all tracking players
                CustomPayload packet = new RailgunFlagsPayload(false, hitEntityResult.getValue());
                ServerPlayNetworking.send(player, packet);

                for (ServerPlayerEntity target : PlayerLookup.tracking(player)) {
                    ServerPlayNetworking.send(target, packet);
                }
            }
        }
    }
}
