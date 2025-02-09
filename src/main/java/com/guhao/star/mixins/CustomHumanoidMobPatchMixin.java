package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.entitypatch.CustomHumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

@Mixin(
        value = {CustomHumanoidMobPatch.class},
        remap = false
)
public abstract class CustomHumanoidMobPatchMixin extends EntityPatch<LivingEntity> {


    @Inject(
            method = {"setAIAsInfantry"},
            at = {@At("HEAD")},
            cancellable = true,
            remap = false
    )
    public void setAIAsInfantry(boolean holdingRanedWeapon, CallbackInfo ci) {
        if (this.getOriginal().hasEffect(Effect.EXECUTED.get())) {
            ci.cancel();
        }
    }

}