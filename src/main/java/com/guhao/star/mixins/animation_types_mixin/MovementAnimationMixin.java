package com.guhao.star.mixins.animation_types_mixin;

import com.guhao.star.regirster.Effect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.MovementAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = MovementAnimation.class,remap = false)
public class MovementAnimationMixin {
    @Inject(method = "getPlaySpeed",at = @At("HEAD"), cancellable = true)
    public void getPlaySpeed(LivingEntityPatch<?> entitypatch, CallbackInfoReturnable<Float> cir) {
        if (entitypatch.getOriginal().hasEffect(Effect.SLOW_TIME.get())) cir.setReturnValue(0.05f);
        if (entitypatch.getOriginal().hasEffect(Effect.DING.get())) cir.setReturnValue(0.0f);
    }
}
