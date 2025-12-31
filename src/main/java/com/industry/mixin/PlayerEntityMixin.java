package com.industry.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private boolean canDoubleJump = true;

    @Unique
    private boolean jumpedLastTick = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void industry$tickMovement(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;

        // Reset when landing
        if (self.isOnGround()) {
            canDoubleJump = true;
            jumpedLastTick = false;
            return;
        }

        Vec3d vel = self.getVelocity();

        // Detect upward motion starting while mid-air
        boolean isTryingToJump = vel.y > 0 && !jumpedLastTick;

        if (canDoubleJump && isTryingToJump) {
            self.setVelocity(vel.x, 0.42, vel.z);
            canDoubleJump = false;
            jumpedLastTick = true;
        }

        jumpedLastTick = vel.y > 0;
    }
}
