package com.guhao.star.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reascer.wom.animation.attacks.BasicMultipleAttackAnimation;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.damagesource.StunType;

@Mixin(value = WOMAnimations.class,remap = false)
public class WOMAnimationMixin {
    @Shadow
    public static StaticAnimation KATANA_AUTO_3;
    @Inject(method = "build",at = @At("TAIL"))
    private static void build(CallbackInfo ci) {
        HumanoidArmature biped = Armatures.BIPED;
        KATANA_AUTO_3 = (new BasicMultipleAttackAnimation(0.2F, 0.3F, 0.2F, 0.75F, (Collider) null, biped.toolR, "biped/combat/katana_auto_3", biped)).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD).addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.6F);
    }
}
