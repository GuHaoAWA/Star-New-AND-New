package com.guhao.star.api.animation.types;


import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.model.Armature;

public class SpecialActionAnimation extends ActionAnimation {
    public SpecialActionAnimation(float convertTime, String path, Armature armature) {
        this(convertTime, Float.MAX_VALUE, path, armature);
    }

    public SpecialActionAnimation(float convertTime, float postDelay, String path, Armature armature) {
        super(convertTime, path, armature);
        this.stateSpectrumBlueprint.clear().newTimePair(0.0F, postDelay).addState(EntityState.UPDATE_LIVING_MOTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).addState(EntityState.CAN_SKILL_EXECUTION, false).newTimePair(0.01F, postDelay).addState(EntityState.TURNING_LOCKED, true).newTimePair(0.0F, Float.MAX_VALUE).addState(EntityState.INACTION, true);
    }

    public <V> SpecialActionAnimation addProperty(AnimationProperty.ActionAnimationProperty<V> propertyType, V value) {
        this.properties.put(propertyType, value);
        return this;
    }

    protected boolean shouldMove(float currentTime) {
        return true;
    }

}
