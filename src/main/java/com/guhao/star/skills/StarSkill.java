package com.guhao.star.skills;

import com.guhao.skills.SacrificeSkill;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.dodge.StepSkill;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.SimpleWeaponInnateSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Set;

import static com.guhao.star.Star.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class StarSkill {
    public static Skill DOUBLE_JUMP;
    public static Skill SEE_THROUGH;
    public static Skill YAMATO_STEP;
    public static Skill HYPER_CHARGE_SKILL;
    public static Skill WUSONG_PASSIVE;
    public static Skill WUSONG_SKILL;
    /** New skills **/
    public static Skill UCHIGATANA_SPIKES;
    public static Skill MORTAL_BLADE;
    public static Skill LETHAL_SLICING;
    public static Skill LORD_OF_THE_STORM;
    public static Skill BODYATTACKFIST;
    public static Skill BODYATTACKSHANK;
    public static Skill LION_CLAW;
    public static Skill SLASH;
    public static Skill YAMATOSKILL;

    public static Skill  SWORD_SLASH;
    public static Skill DUAL_SLASH;
    public static Skill WIND_SLASH;
    public static Skill SPEAR_SLASH;
    public static Skill HEARTPIERCERS;
    public static Skill FATAL_DRAW;
    public StarSkill() {
    }

    public static void registerSkills() {
        SkillManager.register(DoubleJumpSkill::new, Skill.createMoverBuilder().setResource(Skill.Resource.COOLDOWN), MODID, "double_jump");
        SkillManager.register(StepSkill::new, StepSkill.createDodgeBuilder().setAnimations(new ResourceLocation(MODID, "biped/yamato_step_forward"), new ResourceLocation(MODID, "biped/yamato_step_backward"), new ResourceLocation(MODID, "biped/yamato_step_left"), new ResourceLocation(MODID, "biped/yamato_step_right")), MODID, "yamato_step");
        //////////////////////////////
        SkillManager.register(UchigatanaSpikesSkill::new, ConditionalWeaponInnateSkill.createConditionalWeaponInnateBuilder().setSelector((executer) -> executer.getOriginal().isSprinting() ? 1 : 0).setAnimations(new ResourceLocation(MODID, "biped/new/blade_rush_finisher"), new ResourceLocation(MODID, "biped/skill/battojutsu_dash")), MODID, "uchigatanaspikes");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/tachi_auto2")), MODID, "mortalblade");
        SkillManager.register(LethalSlicingSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), MODID, "lethalslicing");
        SkillManager.register(YamatoSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), MODID, "yamatoskill");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/mob_greatsword1")), MODID, "lordofthestorm");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/fist_auto1")), MODID, "bodyattackfist");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/vanilla_lethal_slicing_start")), MODID, "bodyattackshank");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/lion_claw")), MODID, "lionclaw");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/lethal_slicing_third")), MODID, "slash");
        SkillManager.register(SeeThroughSkill::new, SeeThroughSkill.createSeeThroughSkillBuilder(), MODID, "see_through");
        SkillManager.register(HyperChargeSkill::new, PassiveSkill.createPassiveBuilder(), MODID, "hyper_charge");
        SkillManager.register(WuSongPassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.COOLDOWN), MODID, "wusong_passive");
        SkillManager.register(WuSongSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), MODID, "wusong_skill");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/sword_slash")), MODID, "sword_slash");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/dual_slash")), MODID, "dual_slash");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/wind_slash")), MODID, "wind_slash");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/spear_slash")), MODID, "spear_slash");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/spear/heartpiercer")), MODID, "heartpiercers");
        SkillManager.register(SimpleWeaponInnateSkill::new, SimpleWeaponInnateSkill.createSimpleWeaponInnateBuilder().setAnimations(new ResourceLocation(MODID, "biped/new/katana/skill/fatal_draw")), MODID, "fatal_draw");
    }

    @SubscribeEvent
    public static void BuildSkills(SkillBuildEvent event) {
        Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("Build Star Skill");
        DOUBLE_JUMP = event.build(MODID, "double_jump");
        YAMATO_STEP = event.build(MODID, "yamato_step");
        SEE_THROUGH = event.build(MODID, "see_through");
        HYPER_CHARGE_SKILL = event.build(MODID, "hyper_charge");
        WUSONG_PASSIVE = event.build(MODID, "wusong_passive");
        WUSONG_SKILL = event.build(MODID, "wusong_skill");
//////////////////////////////////////////
        /*
        WeaponInnateSkill uchigatanaSpikes = event.build(MODID, "uchigatanaspikes");
        uchigatanaSpikes.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        UCHIGATANA_SPIKES = uchigatanaSpikes;
*/



        WeaponInnateSkill mortalBlade = event.build(MODID, "mortalblade");
        mortalBlade.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        MORTAL_BLADE = mortalBlade;

        WeaponInnateSkill lethalSlicing = event.build(MODID, "lethalslicing");
        lethalSlicing.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(0.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        LETHAL_SLICING = lethalSlicing;

        WeaponInnateSkill lordOfTheStorm = event.build(MODID, "lordofthestorm");
        lordOfTheStorm.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(6))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        LORD_OF_THE_STORM = lordOfTheStorm;

        WeaponInnateSkill bodyAttackFist = event.build(MODID, "bodyattackfist");
        bodyAttackFist.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        BODYATTACKFIST = bodyAttackFist;

        WeaponInnateSkill bodyAttackShank = event.build(MODID, "bodyattackshank");
        bodyAttackShank.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(1.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(1))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(7.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        BODYATTACKSHANK = bodyAttackShank;

        WeaponInnateSkill lionClaw = event.build(MODID, "lionclaw");
        lionClaw.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(100.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(10.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        LION_CLAW = lionClaw;

        WeaponInnateSkill YamatoSkill = event.build(MODID, "yamatoskill");
        YamatoSkill.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(15.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        YAMATOSKILL = YamatoSkill;

        WeaponInnateSkill slash = event.build(MODID, "slash");
        slash.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(25.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(5))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(1.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .registerPropertiesToAnimation();
        SLASH = slash;

        WeaponInnateSkill sword_slash = event.build(MODID, "sword_slash");
        sword_slash.newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(20.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.6F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        SWORD_SLASH = sword_slash;

        WeaponInnateSkill dual_slash = event.build(MODID, "dual_slash");
        dual_slash.newProperty()
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        DUAL_SLASH = dual_slash;


        WeaponInnateSkill wind_slash = event.build(MODID, "wind_slash");
        wind_slash.newProperty()
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.4F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        WIND_SLASH = wind_slash;

        WeaponInnateSkill spear_slash = event.build(MODID, "spear_slash");
        spear_slash.newProperty()
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.25F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.2F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        SPEAR_SLASH = spear_slash;

        WeaponInnateSkill heartpiercers = event.build(MODID, "heartpiercers");
        heartpiercers.newProperty()
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(10.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        HEARTPIERCERS = heartpiercers;


        WeaponInnateSkill fatal_draw = event.build(MODID, "fatal_draw");
        fatal_draw.newProperty()
                .newProperty()
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(1.4F))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2F))
                .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                .registerPropertiesToAnimation();
        FATAL_DRAW = fatal_draw;

    }
}
