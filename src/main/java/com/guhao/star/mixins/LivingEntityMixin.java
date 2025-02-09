package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(at = @At("HEAD"), method = "push", cancellable = true)
    private void collide(Entity p_21294_, CallbackInfo ci) {
        if (p_21294_ instanceof LivingEntity livingEntity) {
            if ((livingEntity.hasEffect(Effect.EXECUTE.get())) || (livingEntity.hasEffect(Effect.EXECUTED.get()))) ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void hurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if (p_21016_.getDirectEntity() instanceof LivingEntity livingEntity) {
            if ((livingEntity.hasEffect(Effect.DING.get()))) cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"),method = "getSpeed",cancellable = true)
    public void getSpeed(CallbackInfoReturnable<Float> cir) {
        if (level.getEntity(getId()) instanceof LivingEntity livingEntity && livingEntity.hasEffect(Effect.DING.get())) cir.setReturnValue(0.0f);
    }
    @Inject(at = @At("HEAD"), method = "isPushable", cancellable = true)
    private void CanBePushed(CallbackInfoReturnable<Boolean> cir) {
        if (level.getEntity(getId()) instanceof Player) cir.setReturnValue(false);
        if (level.getEntity(getId()) instanceof LivingEntity livingEntity && (livingEntity.hasEffect(Effect.EXECUTE.get())) | (livingEntity.hasEffect(Effect.EXECUTED.get()))) cir.setReturnValue(false);
    }


    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void tick(CallbackInfo ci) {
//        if (level.getEntity(getId()) instanceof LivingEntity livingEntity && livingEntity.hasEffect(Effect.DING.get()) && livingEntity.getHealth() > 0 && livingEntity instanceof MowzieEntity) ci.cancel();
    }
}
