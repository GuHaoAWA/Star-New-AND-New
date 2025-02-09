package com.guhao.star.mixins;

import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.*;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.weaponinnate.BladeRushSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

import java.util.Map;
import java.util.UUID;

@Mixin(value = BladeRushSkill.class,remap = false)
public class BladeRushSkillMixin extends WeaponInnateSkill {
    public BladeRushSkillMixin(Builder builder, Map<EntityType<?>, StaticAnimation> tryAnimations) {
        super(builder);
        this.tryAnimations = tryAnimations;

        this.comboAnimations = new StaticAnimation[3];
        this.comboAnimations[0] = Animations.BLADE_RUSH_COMBO1;
        this.comboAnimations[1] = Animations.BLADE_RUSH_COMBO2;
        this.comboAnimations[2] = Animations.BLADE_RUSH_COMBO3;
    }

    @Shadow
    private static final SkillDataManager.SkillDataKey<Integer> COMBO_COUNT = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("444a1a6a-c2f1-11eb-8529-0242ac130003");
    @Mutable
    @Final
    @Shadow
    private final StaticAnimation[] comboAnimations;
    @Mutable
    @Final
    @Shadow
    private final Map<EntityType<?>, StaticAnimation> tryAnimations;
    /**
     * @author
     * @reason
     */
    @Overwrite
    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO1).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO2).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_COMBO3).phases[0].addProperties(this.properties.get(0).entrySet());
        ((AttackAnimation)Animations.BLADE_RUSH_EXECUTE_BIPED).phases[0].addProperties(this.properties.get(0).entrySet());
        return this;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static BladeRushSkill.Builder createBladeRushBuilder() {
        return (new BladeRushSkill.Builder()).setCategory(SkillCategories.WEAPON_INNATE)
                .setResource(Resource.WEAPON_INNATE_ENERGY)
                .putTryAnimation(EntityType.ZOMBIE, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.HUSK, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.DROWNED, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.SKELETON, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.STRAY, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.CREEPER, Animations.BLADE_RUSH_TRY)
                .putTryAnimation(EntityType.IRON_GOLEM, Animations.BLADE_RUSH_TRY);
    }

}
