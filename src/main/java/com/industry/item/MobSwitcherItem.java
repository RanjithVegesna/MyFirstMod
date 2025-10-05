package com.industry.item;

import com.industry.Mod;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MobSwitcherItem extends Item {

    private static BlockPos lastPos;

    public static Item Chisel = new MobSwitcherItem(new Item.Settings());

    public static final Map<EntityType<?>, EntityType<?>> ENTITY_MAP = new HashMap<>
            (Map.ofEntries(
                    // Villager Cycle: humanoid NPCs and illagers
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
            world.setBlockState(player.getBlockPos().up(2), Blocks.LIGHT.getDefaultState());
            BlockPos newPos = player.getBlockPos().up(2);

            if (lastPos != null && world.getBlockState(lastPos).getBlock() != Blocks.AIR && !lastPos.equals(newPos)) {
                world.setBlockState(lastPos, Blocks.AIR.getDefaultState());
            }

            lastPos = newPos;
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {

        World world = user.getWorld();
        EntityType<?> nextType = ENTITY_MAP.get(entity.getType());
        if (nextType == null) {
            return ActionResult.PASS;
        }

        if (hand == Hand.MAIN_HAND) {
            if (!world.isClient) {
                Entity newEntity = nextType.create(world);
                if (ENTITY_MAP.containsKey(entity.getType())) {

                    Mod.LOGGER.info("Using MobSwitcherItem for " + entity.getType());
                    assert newEntity != null;

                    newEntity.refreshPositionAndAngles(
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            entity.getYaw(),
                            entity.getPitch()
                    );

                        world.spawnEntity(newEntity);

                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}
