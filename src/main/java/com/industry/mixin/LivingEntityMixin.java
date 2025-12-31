package com.industry.mixin;

import com.industry.ModSoundEvents;
import com.industry.serverSideImplementers.PlayerTickHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(
            method = "onDeath",
            at = @At("TAIL")
    )
    private void industry$onDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if ((self instanceof PlayerEntity)) {
            return;
        }

        World world = self.getWorld();

        Box box = new Box(self.getBlockPos()).expand(50);

        List<PlayerEntity> playerEntities = world.getEntitiesByClass(PlayerEntity.class, box,e -> true);
        for (PlayerEntity player : playerEntities) {
            player.playSound(ModSoundEvents.DEATH_SOUND, 0.7f, 0.9f);;
        }
    }
}
