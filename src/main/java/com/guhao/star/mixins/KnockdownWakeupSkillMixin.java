package com.guhao.star.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.skill.dodge.KnockdownWakeupSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(
        value = {KnockdownWakeupSkill.class},
        remap = false
)
public class KnockdownWakeupSkillMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isExecutableState(PlayerPatch<?> executer) {
        EntityState playerState = executer.getEntityState();
        return !executer.isUnstable() && (!playerState.hurt() || playerState.knockDown()) && !executer.getOriginal().onClimbable();
    }
}
