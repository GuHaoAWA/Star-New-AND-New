package com.guhao.star.api.animation.types;

import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;

public class ExAttackAnimation extends AttackAnimation {
    public final Phase[] phases;
    public ExAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature, Phase[] phases) {
        this(convertTime, path, armature, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
    }
    public ExAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, path, armature, new Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, hand, colliderJoint, collider));
    }
    public ExAttackAnimation(float convertTime, String path, Armature armature, Phase... phases) {
        super(convertTime, path, armature);

        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        this.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true);
        this.phases = phases;
        this.stateSpectrumBlueprint.clear();

        for (Phase phase : phases) {
            if (!phase.noStateBind) {
                this.bindPhaseState(phase);
            }
        }
    }
    @Override
    protected void bindPhaseState(Phase phase) {
        float preDelay = phase.preDelay;
        if (preDelay == 0.0F) {
            preDelay += 0.01F;
        }

        this.stateSpectrumBlueprint.newTimePair(phase.start, preDelay)
                .addState(EntityState.PHASE_LEVEL, 1)
                .newTimePair(phase.start, phase.contact + 0.01F)
                .addState(EntityState.CAN_SKILL_EXECUTION, false)
                .newTimePair(phase.start, phase.recovery)
                .addState(EntityState.MOVEMENT_LOCKED, true)
                .addState(EntityState.LOCKON_ROTATE, true)

                .addState(EntityState.TURNING_LOCKED, true)
                .addState(EntityState.UPDATE_LIVING_MOTION, false)
                .addState(EntityState.CAN_BASIC_ATTACK, false)
                .newTimePair(phase.start, phase.end)
                .addState(EntityState.INACTION, true)

                .newTimePair(preDelay, phase.contact + 0.01F).addState(EntityState.ATTACKING, true)
                .addState(EntityState.PHASE_LEVEL, 2).newTimePair(phase.contact + 0.01F, phase.end)
                .addState(EntityState.PHASE_LEVEL, 3);
    }
}
