package com.industry.mixin;

import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class BoatMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    public void boatTick(CallbackInfo ci) {
        BoatEntity self = (BoatEntity) (Object) this;

        Vec3d vel = self.getVelocity();

        Vec3d forward = Vec3d.fromPolar(0, self.getYaw());
        forward = new Vec3d(forward.x, 0, forward.z).normalize();

        double forwardSpeed = vel.dotProduct(forward);

        Vec3d forwardVel = forward.multiply(forwardSpeed);
        Vec3d sidewaysVel = vel.subtract(forwardVel);

        sidewaysVel = sidewaysVel.multiply(0.5);



        if (sidewaysVel.length() > 0.2 && forwardVel.lengthSquared() < 20) {
            self.setVelocity(forwardVel.multiply(1.1).add(sidewaysVel));
            for (double i = -0.25; i < 0.25; i+=0.1) {
                for (double j = -0.25; j < 0.25; j+=0.1) {
                    for (double k = -0.25; k < 0.25; k+=0.1) {
                        self.getEntityWorld().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, self.getX() + i, self.getY() + 0.5 + j, self.getZ() + k, i/ 10, j/10, k/10);
                    }
                }
            }
            return;
        }

        self.setVelocity(forwardVel.add(sidewaysVel));
    }

    @Redirect(method = "checkLocation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;getNearbySlipperiness()F"))
    public float getNearbySlipperiness(BoatEntity self) {
        return 0.98f;
    }

    @Redirect(method = "")
}
