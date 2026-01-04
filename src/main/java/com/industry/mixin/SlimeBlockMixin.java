package com.industry.mixin;

import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBlockMixin {

    @Redirect(method = "onEntityLand", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SlimeBlock;bounce(Lnet/minecraft/entity/Entity;)V"))
    private void industry$bounce(SlimeBlock instance, Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0) {
            double d = entity instanceof LivingEntity ? 3.0 : 0.8;
            entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
            entity.velocityModified = true;
            entity.velocityDirty = true;
            entity.setOnGround(false);
        }
    }
}
