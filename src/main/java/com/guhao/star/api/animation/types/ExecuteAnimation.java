package com.guhao.star.api.animation.types;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import yesman.epicfight.api.animation.property.AnimationProperty.ActionAnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StateSpectrum;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.config.ConfigurationIngame;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

import java.util.function.Function;


public class ExecuteAnimation extends AttackAnimation {


    protected final StateSpectrum.Blueprint stateUtilsBlueprint = new StateSpectrum.Blueprint();
    private final StateSpectrum stateUtils = new StateSpectrum();
    protected final float attackstart;
    protected final float attackwinopen;
    protected final float attackwinclose;
    protected final float skillwinopen;
    protected final float skillwinclose;
    protected final float attackend;
    protected final float lockon;
    protected final float lockoff;

    public static final Function<DamageSource, AttackResult.ResultType> DODGEABLE_SOURCE_VALIDATOR = (damagesource) -> {
        if (damagesource instanceof EntityDamageSource
                && !damagesource.isBypassArmor()
                && !damagesource.isBypassInvul()) {
            return AttackResult.ResultType.MISSED;
        }

        return AttackResult.ResultType.SUCCESS;
    };

    public ExecuteAnimation(float convertTime, float attackstart, float attackend, float attackwinopen, float attackwinclose, float skillwinopen, float skillwinclose, float lockon, float lockoff, String path, Armature armature, AttackAnimation.Phase... phases) {
        super(convertTime, path, armature, phases);


        this.attackstart = attackstart;
        this.attackwinopen = attackwinopen;
        this.attackwinclose = attackwinclose;
        this.skillwinopen = skillwinopen;
        this.skillwinclose = skillwinclose;
        this.lockon = lockon;
        this.lockoff = lockoff;
        this.attackend = attackend;
        this.stateUtilsBlueprint.clear();
        AttackAnimation.Phase[] var13 = phases;
        int var14 = phases.length;
        this.addProperty(ActionAnimationProperty.STOP_MOVEMENT, true);
    }


    @Override
    protected void bindPhaseState(Phase phase) {
        float preDelay = phase.preDelay;

        this.stateSpectrumBlueprint
                .newTimePair(attackstart, attackwinopen)
                .addState(EntityState.CAN_BASIC_ATTACK, false)

                .newTimePair( phase.contact, phase.recovery)
                .addState(EntityState.CAN_BASIC_ATTACK, false)

                .newTimePair(phase.start, preDelay)
                .addState(EntityState.PHASE_LEVEL, 1)

                .newTimePair(phase.start, phase.end+1F)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false)

                .newTimePair(phase.start, phase.end)
                .addState(EntityState.INACTION, true)
                .addState(EntityState.CAN_BASIC_ATTACK, false)
                .addState(EntityState.MOVEMENT_LOCKED, true)

                .newTimePair(preDelay, phase.contact + 0.01F)
                .addState(EntityState.ATTACKING, true)
                .addState(EntityState.PHASE_LEVEL, 2)

                .newTimePair(phase.contact + 0.01F, phase.end)
                .addState(EntityState.PHASE_LEVEL, 3)
                .addState(EntityState.TURNING_LOCKED, true)

                .newTimePair(attackwinclose ,skillwinclose)
                .addState(EntityState.PHASE_LEVEL, 3)
                .addState(EntityState.TURNING_LOCKED, true)
                .newTimePair(0.0F, Float.MAX_VALUE)
                .addState(EntityState.ATTACK_RESULT, DODGEABLE_SOURCE_VALIDATOR);

    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);

        boolean stiffAttack = entitypatch.getOriginal().level.getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS).get();

        if (!isEnd && !nextAnimation.isMainFrameAnimation() && entitypatch.isLogicalClient() && !stiffAttack) {
            float playbackSpeed = ConfigurationIngame.A_TICK * this.getPlaySpeed(entitypatch);
            entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
        }
    }
}