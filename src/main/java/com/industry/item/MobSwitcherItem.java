package com.industry.item;

import com.industry.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MobSwitcherItem extends Item {

    private static BlockPos lastPos;
    private static boolean wasInHandInPreviousTick = false;

    public static Item Chisel = new MobSwitcherItem(new Item.Settings());

    public static final Map<EntityType<?>, EntityType<?>> ENTITY_MAP = new HashMap<>
            (Map.ofEntries(
                    Map.entry(EntityType.ZOMBIE_VILLAGER, EntityType.VILLAGER),
                    Map.entry(EntityType.VILLAGER, EntityType.WANDERING_TRADER),
                    Map.entry(EntityType.WANDERING_TRADER, EntityType.PILLAGER),
                    Map.entry(EntityType.PILLAGER, EntityType.VINDICATOR),
                    Map.entry(EntityType.VINDICATOR, EntityType.EVOKER),
                    Map.entry(EntityType.EVOKER, EntityType.WITCH),
                    Map.entry(EntityType.WITCH, EntityType.ZOMBIE_VILLAGER),

                    // Skeleton Cycle: bone undead variants
                    Map.entry(EntityType.SKELETON, EntityType.STRAY),
                    Map.entry(EntityType.STRAY, EntityType.BOGGED),
                    Map.entry(EntityType.BOGGED, EntityType.WITHER_SKELETON),
                    Map.entry(EntityType.WITHER_SKELETON, EntityType.SKELETON),

                    // Zombie Cycle: zombie-like undead variants
                    Map.entry(EntityType.HUSK, EntityType.ZOMBIE),
                    Map.entry(EntityType.ZOMBIE, EntityType.GIANT),
                    Map.entry(EntityType.GIANT, EntityType.ZOMBIFIED_PIGLIN),
                    Map.entry(EntityType.ZOMBIFIED_PIGLIN, EntityType.HUSK),

                    // Boss Cycle: powerful hostile mobs
                    Map.entry(EntityType.WARDEN, EntityType.WITHER),
                    Map.entry(EntityType.WITHER, EntityType.ENDER_DRAGON),
                    Map.entry(EntityType.ENDER_DRAGON, EntityType.WARDEN),

                    // Passive Animals Cycle: common farm animals
                    Map.entry(EntityType.COW, EntityType.SHEEP),
                    Map.entry(EntityType.SHEEP, EntityType.PIG),
                    Map.entry(EntityType.PIG, EntityType.CHICKEN),
                    Map.entry(EntityType.CHICKEN, EntityType.RABBIT),
                    Map.entry(EntityType.RABBIT, EntityType.COW),

                    // Aquatic Cycle: water mobs
                    Map.entry(EntityType.COD, EntityType.SALMON),
                    Map.entry(EntityType.SALMON, EntityType.PUFFERFISH),
                    Map.entry(EntityType.PUFFERFISH, EntityType.TROPICAL_FISH),
                    Map.entry(EntityType.TROPICAL_FISH, EntityType.COD),

                    Map.entry(EntityType.GHAST, EntityType.FIREBALL),

                    // Spider Cycle: arachnids
                    Map.entry(EntityType.SPIDER, EntityType.CAVE_SPIDER),
                    Map.entry(EntityType.CAVE_SPIDER, EntityType.SPIDER),

                    // Slime Cycle: different size slimes
                    Map.entry(EntityType.SLIME, EntityType.MAGMA_CUBE),
                    Map.entry(EntityType.MAGMA_CUBE, EntityType.SLIME),

                    Map.entry(EntityType.ENDERMAN, EntityType.ENDERMITE),
                    Map.entry(EntityType.ENDERMITE, EntityType.SHULKER),
                    Map.entry(EntityType.SHULKER, EntityType.ENDERMAN),

                    Map.entry(EntityType.WOLF, EntityType.CAT),
                    Map.entry(EntityType.CAT, EntityType.FOX),
                    Map.entry(EntityType.FOX, EntityType.WOLF),

                    Map.entry(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM),
                    Map.entry(EntityType.SNOW_GOLEM, EntityType.IRON_GOLEM),

                    Map.entry(EntityType.HORSE, EntityType.DONKEY),
                    Map.entry(EntityType.DONKEY, EntityType.MULE),
                    Map.entry(EntityType.MULE, EntityType.HORSE),

                    Map.entry(EntityType.TURTLE, EntityType.TURTLE),

                    // Flying mobs cycle: parrots, bats, phantoms
                    Map.entry(EntityType.PARROT, EntityType.BAT),
                    Map.entry(EntityType.BAT, EntityType.PHANTOM),
                    Map.entry(EntityType.PHANTOM, EntityType.PARROT)

                    ));


    public MobSwitcherItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (!(entity instanceof PlayerEntity player)) {return;}



        if (player.getMainHandStack().getItem() instanceof MobSwitcherItem || player.getOffHandStack().getItem() instanceof MobSwitcherItem) {
            world.setBlockState(player.getBlockPos().up(1), Blocks.LIGHT.getDefaultState());
            BlockPos newPos = player.getBlockPos().up(1);

            if (lastPos != null && world.getBlockState(lastPos).getBlock() != Blocks.AIR && !lastPos.equals(newPos)) {
                world.setBlockState(lastPos, Blocks.AIR.getDefaultState());
            }
            wasInHandInPreviousTick = true;
            lastPos = newPos;
        }
        else {
            if (wasInHandInPreviousTick) {
                world.setBlockState(lastPos, Blocks.AIR.getDefaultState());
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        double maxDistance = 500.0;

        if (!world.isClient) {

            // Raycast for entity from player's eye position
            EntityHitResult entityHitResult = rayCastEntity(user, maxDistance);

            if (entityHitResult != null) {
                Entity targetEntity = entityHitResult.getEntity();

                if (targetEntity instanceof LivingEntity living && (ENTITY_MAP.containsKey(living.getType()))) {
                    EntityType<?> nextType = ENTITY_MAP.get(living.getType());
                    Entity newEntity = nextType.create(world);
                    if (newEntity != null) {
                        newEntity.refreshPositionAndAngles(
                                living.getX(), living.getY(), living.getZ(),
                                living.getYaw(), living.getPitch()
                        );
                        world.spawnEntity(newEntity);
                        ItemUtils.spawnParticles(world, living, ParticleTypes.HAPPY_VILLAGER, 100);
                        living.remove(Entity.RemovalReason.DISCARDED);
                        Mod.LOGGER.info("Switched mob {} to {}", living.getType(), nextType);

                        ItemUtils.getRayPoints(world, ParticleTypes.FLAME, user.getEyePos(), user.getRotationVec(1), maxDistance, 0.1);

                        return TypedActionResult.success(user.getStackInHand(hand));
                    }
                }
            }
            ItemUtils.getRayPoints(world, ParticleTypes.SOUL_FIRE_FLAME, user.getEyePos(), user.getRotationVec(1), maxDistance, 0.1);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    // Helper method to raycast entities up to maxDistance
    private EntityHitResult rayCastEntity(PlayerEntity player, double maxDistance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d end = start.add(look.x * maxDistance, look.y * maxDistance, look.z * maxDistance);

        // Get all entities in the path of the raycast
        Box box = player.getBoundingBox().stretch(look.multiply(maxDistance)).expand(1.0D, 1.0D, 1.0D);
        List<Entity> entities = player.getWorld().getEntitiesByClass(Entity.class, box, entity -> entity instanceof LivingEntity);

        EntityHitResult closestHit = null;
        double closestDistance = maxDistance;

        for (Entity entity : entities) {
            Box entityBox = entity.getBoundingBox().expand(0.3D);
            Optional<Vec3d> hitPos = entityBox.raycast(start, end);

            if (hitPos.isPresent()) {
                double distance = start.distanceTo(hitPos.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestHit = new EntityHitResult(entity, hitPos.get());
                }
            }
        }

        return closestHit;
    }

}
