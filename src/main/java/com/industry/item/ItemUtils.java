package com.industry.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import java.util.*;

public class ItemUtils {
    public static boolean isInInventory(PlayerEntity player, Item item) {
        for (ItemStack stack : player.getInventory().main){
            if (stack.getItem() == item){
                return true;
            }
        }
        return false;
    }

    public static boolean isInEitherHand(PlayerEntity player,  Item item) {
        return player.getMainHandStack().getItem() == item || player.getOffHandStack().getItem() == item;
    }

    public static void spawnParticles(World world, LivingEntity entity, ParticleEffect particleType, int particleCount) {
        double centerX = entity.getX();
        double centerY = entity.getY() + 1; // slightly above head
        double centerZ = entity.getZ();

        for (int i = 0; i < particleCount; i++) {

            double offsetX = (world.random.nextDouble() - 0.5) * 1.5;
            double offsetY = world.random.nextDouble();
            double offsetZ = (world.random.nextDouble() - 0.5) * 1.5;


            double velocityX = (world.random.nextDouble() - 0.5) * 0.1;
            double velocityY = (world.random.nextDouble()) * 0.1;
            double velocityZ = (world.random.nextDouble() - 0.5) * 0.1;


            world.addParticle(particleType,
                    centerX + offsetX,
                    centerY + offsetY,
                    centerZ + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ);
        }
    }
    public static Map<Block, Block> createBlockCycle(Block... blocks) {
        Map<Block, Block> cycle = new HashMap<>();
        for (int i = 0; i < blocks.length; i++) {
            Block current = blocks[i];
            Block next = blocks[(i + 1) % blocks.length]; // wrap-around
            cycle.put(current, next);
        }
        return cycle;
    }

    @SuppressWarnings("unused")
    public static List<BlockPos> ifBoxContains(Box box, World world) {
        List<BlockPos> blocks = new ArrayList<>();

        int minX = (int) Math.floor(box.minX);
        int minY = (int) Math.floor(box.minY);
        int minZ = (int) Math.floor(box.minZ);
        int maxX = (int) Math.ceil(box.maxX);
        int maxY = (int) Math.ceil(box.maxY);
        int maxZ = (int) Math.ceil(box.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    VoxelShape blockShape = state.getCollisionShape(world, pos);

                    // Create a box relative to the block (0-1 cube)
                    Box relativeBox = new Box(
                            box.minX - x, box.minY - y, box.minZ - z,
                            box.maxX - x, box.maxY - y, box.maxZ - z
                    );

                    VoxelShape boxShape = VoxelShapes.cuboid(relativeBox);

                    // Check if the block's shape intersects the boxShape
                    if (VoxelShapes.matchesAnywhere(blockShape, boxShape, BooleanBiFunction.AND)) {
                        blocks.add(pos);
                    }
                }
            }
        }
        return blocks;
    }

    public static void getRayPoints(World world, ParticleEffect particleType, Vec3d start, Vec3d direction, double maxDistance, double stepSize) {
        if (world.isClient) return; // Do nothing on client

        ServerWorld serverWorld = (ServerWorld) world;
        Vec3d normDirection = direction.normalize();

        for (double d = 0; d <= maxDistance; d += stepSize) {
            Vec3d point = start.add(normDirection.multiply(d));

            serverWorld.spawnParticles(
                    particleType,      // Particle type (e.g. ParticleTypes.END_ROD)
                    point.x, point.y, point.z, // Particle position
                    1,                 // Count
                    0.00, 0.00, 0.00,  // Small spread
                    0.00               // Particle speed
            );
        }
    }
    public static List<EntityHitResult> rayCastEntities(PlayerEntity player, double maxDistance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d end = start.add(look.multiply(maxDistance));

        // Get all entities in the ray's path
        Box box = player.getBoundingBox().stretch(look.multiply(maxDistance)).expand(1.0D);
        List<Entity> entities = player.getWorld().getEntitiesByClass(Entity.class, box,
                entity -> entity instanceof LivingEntity && entity != player && entity.isAlive());

        List<EntityHitResult> hitResults = new ArrayList<>();

        for (Entity entity : entities) {
            Box entityBox = entity.getBoundingBox().expand(0.3D);
            Optional<Vec3d> hitPos = entityBox.raycast(start, end);

            hitPos.ifPresent(vec3d -> hitResults.add(new EntityHitResult(entity, vec3d)));
        }

        // Sort by distance from start to ensure correct hit order
        hitResults.sort(Comparator.comparingDouble(hit -> start.distanceTo(hit.getPos())));

        return hitResults;
    }


}
