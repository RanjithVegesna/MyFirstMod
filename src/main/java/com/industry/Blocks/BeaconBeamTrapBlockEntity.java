package com.industry.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.lang.reflect.GenericDeclaration;

public class BeaconBeamTrapBlockEntity extends BlockEntity {

    private Direction direction = Direction.NORTH;

    public BeaconBeamTrapBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BEACON_BEAM_TRAP, pos, state);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        markDirty();
    }

    public static void tick(World world, BlockPos pos, BlockState state, BeaconBeamTrapBlockEntity blockEntity) {
        if (world.isClient) return;

        Vec3d start = Vec3d.ofCenter(pos);
        Vec3d direction = Vec3d.of(blockEntity.direction.getVector()).normalize();

        double maxDistance = 100;

        Vec3d end = start.add(direction.multiply(maxDistance));

        BlockHitResult hitResult = world.raycast(
                new RaycastContext(
                        start.add(direction), end,
                        RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE,
                        ShapeContext.absent()
                )
        );

        double beamLength = maxDistance;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            beamLength = hitResult.getPos().distanceTo(start);
        }

        Vec3d dir = Vec3d.of(blockEntity.direction.getVector()).normalize();
        Vec3d endT = start.add(dir.multiply(beamLength));

        Box box = new Box(start, endT).expand(1.0);

        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, box, e -> true)) {
            entity.timeUntilRegen = 0;
            entity.hurtTime = 0;
            entity.maxHurtTime = 0;
            entity.damage(world.getDamageSources()., 1.0f);
        }
    }
}
