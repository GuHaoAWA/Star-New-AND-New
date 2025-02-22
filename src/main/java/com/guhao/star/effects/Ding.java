package com.guhao.star.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class Ding extends MobEffect {
    public Ding() {
        super(MobEffectCategory.BENEFICIAL, -13261);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.ding";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }




}
