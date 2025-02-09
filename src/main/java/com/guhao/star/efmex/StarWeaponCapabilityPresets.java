package com.guhao.star.efmex;

import com.google.common.collect.Maps;
import com.guhao.star.skills.StarSkill;
import com.guhao.star.skills.WuSongPassive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.CapabilityItem.Styles;
import yesman.epicfight.world.capabilities.item.CapabilityItem.WeaponCategories;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Map;
import java.util.function.Function;

public class StarWeaponCapabilityPresets {
    public static final Function<Item, CapabilityItem.Builder> WUSONG = (item) ->
            WeaponCapability.builder().category(WeaponCategories.TACHI).styleProvider((entitypatch) -> {
        if (entitypatch instanceof PlayerPatch<?> playerpatch) {
            if (playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(WuSongPassive.SHEATH) && playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(WuSongPassive.SHEATH)) {
                return Styles.SHEATH;
            }
        }

        return Styles.TWO_HAND;
    }).passiveSkill(StarSkill.WUSONG_PASSIVE)
            .hitSound(EpicFightSounds.BLADE_HIT)
            .collider(ColliderPreset.TACHI)
            .canBePlacedOffhand(false)
            .newStyleCombo(Styles.SHEATH,
                    Animations.UCHIGATANA_SHEATHING_AUTO,
                    Animations.UCHIGATANA_SHEATHING_DASH,
                    Animations.UCHIGATANA_SHEATH_AIR_SLASH)
            .newStyleCombo(Styles.TWO_HAND,
                    Animations.TACHI_AUTO1,
                    Animations.TACHI_AUTO2,
                    Animations.TACHI_AUTO3,
                    Animations.TACHI_DASH,
                    Animations.SWORD_DUAL_AIR_SLASH)
            .newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
            .innateSkill(Styles.SHEATH, (itemstack) -> StarSkill.WUSONG_SKILL)
            .innateSkill(Styles.TWO_HAND, (itemstack) -> StarSkill.WUSONG_SKILL)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
            .livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);

    public static final Function<Item, CapabilityItem.Builder> UCHIGATANA_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.UCHIGATANA).styleProvider((entitypatch) -> {
            if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                if (playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(BattojutsuPassive.SHEATH) && (Boolean)playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(BattojutsuPassive.SHEATH)) {
                    return Styles.SHEATH;
                }
            }

            return Styles.TWO_HAND;
        }).passiveSkill(EpicFightSkills.BATTOJUTSU_PASSIVE).hitSound(EpicFightSounds.BLADE_HIT).collider(ColliderPreset.UCHIGATANA).canBePlacedOffhand(false).newStyleCombo(Styles.SHEATH, new StaticAnimation[]{Animations.UCHIGATANA_SHEATHING_AUTO, Animations.UCHIGATANA_SHEATHING_DASH, Animations.UCHIGATANA_SHEATH_AIR_SLASH}).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.UCHIGATANA_AUTO1, Animations.UCHIGATANA_AUTO2, Animations.UCHIGATANA_AUTO3, Animations.UCHIGATANA_DASH, Animations.UCHIGATANA_AIR_SLASH}).newStyleCombo(Styles.MOUNT, new StaticAnimation[]{Animations.SWORD_MOUNT_ATTACK}).innateSkill(Styles.SHEATH, (itemstack) -> {
            return StarSkill.UCHIGATANA_SPIKES;
        }).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.UCHIGATANA_SPIKES;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_WALK_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA).livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.UCHIGATANA_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> TACHI_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.TACHI).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.TACHI).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.TACHI_AUTO1, Animations.TACHI_AUTO2, Animations.TACHI_AUTO3, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH}).newStyleCombo(Styles.MOUNT, new StaticAnimation[]{Animations.SWORD_MOUNT_ATTACK}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.MORTAL_BLADE;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> TACHI_2 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.TACHI).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.TACHI).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.TACHI_AUTO1, Animations.TACHI_AUTO2, Animations.TACHI_AUTO3, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH}).newStyleCombo(Styles.MOUNT, new StaticAnimation[]{Animations.SWORD_MOUNT_ATTACK}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.LETHAL_SLICING;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> STORM_RULER = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.GREATSWORD).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.GREATSWORD).swingSound(EpicFightSounds.WHOOSH_BIG).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.GREATSWORD_AUTO1, Animations.GREATSWORD_AUTO2, Animations.GREATSWORD_DASH, Animations.GREATSWORD_AIR_SLASH}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.LORD_OF_THE_STORM;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> SWORD_1 = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.SWORD).styleProvider((playerpatch) -> {
            return playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SWORD ? Styles.TWO_HAND : Styles.ONE_HAND;
        }).collider(ColliderPreset.SWORD).newStyleCombo(Styles.ONE_HAND, new StaticAnimation[]{Animations.SWORD_AUTO1, Animations.SWORD_AUTO2, Animations.SWORD_AUTO3, Animations.SWORD_DASH, Animations.SWORD_AIR_SLASH}).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2, Animations.SWORD_DUAL_AUTO3, Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH}).newStyleCombo(Styles.MOUNT, new StaticAnimation[]{Animations.SWORD_MOUNT_ATTACK}).innateSkill(Styles.ONE_HAND, (itemstack) -> {
            return StarSkill.BODYATTACKFIST;
        }).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return EpicFightSkills.DANCING_EDGE;
        }).livingMotionModifier(Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON).weaponCombinationPredicator((entitypatch) -> {
            return EpicFightCapabilities.getItemStackCapability(((LivingEntity)entitypatch.getOriginal()).getOffhandItem()).getWeaponCategory() == WeaponCategories.SWORD;
        });
        if (item instanceof TieredItem tieredItem) {
            int harvestLevel = tieredItem.getTier().getLevel();
            builder.addStyleAttibutes(Styles.COMMON, Pair.of((Attribute)EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier(0.5 + 0.2 * (double)harvestLevel)));
            builder.addStyleAttibutes(Styles.COMMON, Pair.of((Attribute)EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(1)));
            builder.hitSound(tieredItem.getTier() == Tiers.WOOD ? EpicFightSounds.BLUNT_HIT : EpicFightSounds.BLADE_HIT);
            builder.hitParticle(tieredItem.getTier() == Tiers.WOOD ? (HitParticleType)EpicFightParticles.HIT_BLUNT.get() : (HitParticleType)EpicFightParticles.HIT_BLADE.get());
        }

        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> ZWEI = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.GREATSWORD).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.GREATSWORD).swingSound(EpicFightSounds.WHOOSH_BIG).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{StarAnimations.ZWEI_AUTO1, StarAnimations.ZWEI_AUTO2, StarAnimations.ZWEI_AUTO3, StarAnimations.ZWEI_DASH, StarAnimations.ZWEI_AIRSLASH}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.BODYATTACKSHANK;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> DRAGONSLAYER = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(StarWeaponCategory.DRAGONSLAYER).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.GREATSWORD).swingSound(EpicFightSounds.WHOOSH_BIG).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{StarAnimations.OLD_GREATSWORD_AUTO1, StarAnimations.OLD_GREATSWORD_AUTO2, Animations.BIPED_MOB_LONGSWORD2, StarAnimations.OLD_GREATSWORD_DASH, StarAnimations.OLD_GREATSWORD_AIRSLASH}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.LION_CLAW;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> YAMATO = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(StarWeaponCategory.YAMATO)
                .styleProvider((playerpatch) -> CapabilityItem.Styles.TWO_HAND)
                .collider(YamatoColliderPreset.YAMATO)
                .swingSound(EpicFightSounds.WHOOSH)
                .hitSound(EpicFightSounds.BLADE_HIT)
                .canBePlacedOffhand(false)
                .newStyleCombo(Styles.TWO_HAND, StarAnimations.YAMATO_AUTO1, StarAnimations.YAMATO_AUTO2, StarAnimations.YAMATO_AUTO3, StarAnimations.YAMATO_AUTO4, StarAnimations.YAMATO_STRIKE1, StarAnimations.YAMATO_POWER3_REPEAT, StarAnimations.YAMATO_DASH, StarAnimations.YAMATO_AIRSLASH)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> StarSkill.YAMATOSKILL)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, StarAnimations.YAMATO_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE,StarAnimations.YAMATO_RUN)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN,StarAnimations.YAMATO_RUN)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK,StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM,StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, StarAnimations.YAMATO_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, StarAnimations.YAMATO_GUARD);
        return builder;
    };
    public static final Function<Item, CapabilityItem.Builder> S_LONGSWORD = (item) ->
            WeaponCapability.builder()
                    .category(StarWeaponCategory.S_LONGSWORD)
                    .styleProvider((playerpatch) -> {
                        if (playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD) {
                            return CapabilityItem.Styles.ONE_HAND;
                        } else if (playerpatch instanceof PlayerPatch<?> tplayerpatch) {
                            return tplayerpatch.getSkill(SkillSlots.WEAPON_INNATE).isActivated() ? CapabilityItem.Styles.OCHS : CapabilityItem.Styles.TWO_HAND;
                        }

                        return CapabilityItem.Styles.TWO_HAND;
                    })
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .collider(ColliderPreset.LONGSWORD)
                    .canBePlacedOffhand(false)
                    .newStyleCombo(CapabilityItem.Styles.ONE_HAND, StarAnimations.SWORD_ONEHAND_AUTO1, StarAnimations.SWORD_ONEHAND_AUTO2, StarAnimations.SWORD_ONEHAND_AUTO3, StarAnimations.SWORD_ONEHAND_AUTO4, StarAnimations.SWORD_ONEHAND_DASH, Animations.SWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, StarAnimations.TACHI_TWOHAND_AUTO_1, StarAnimations.TACHI_TWOHAND_AUTO_2, StarAnimations.TACHI_TWOHAND_AUTO_3, StarAnimations.TACHI_TWOHAND_AUTO_4, StarAnimations.LONGSWORD_OLD_DASH,Animations.LONGSWORD_AIR_SLASH)
                    .newStyleCombo(CapabilityItem.Styles.OCHS, Animations.LONGSWORD_LIECHTENAUER_AUTO1, Animations.LONGSWORD_LIECHTENAUER_AUTO2, Animations.LONGSWORD_LIECHTENAUER_AUTO3, Animations.LONGSWORD_DASH, Animations.LONGSWORD_AIR_SLASH)
                    .innateSkill(CapabilityItem.Styles.ONE_HAND, (itemstack) -> EpicFightSkills.SHARP_STAB)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> EpicFightSkills.LIECHTENAUER)
                    .innateSkill(CapabilityItem.Styles.OCHS, (itemstack) -> EpicFightSkills.LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.WALK, Animations.BIPED_WALK_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.CHASE, Animations.BIPED_WALK_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.RUN, Animations.BIPED_RUN_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.SNEAK, Animations.BIPED_HOLD_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.KNEEL, Animations.BIPED_HOLD_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.JUMP, Animations.BIPED_HOLD_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.SWIM, Animations.BIPED_HOLD_LONGSWORD)
                    .livingMotionModifier(CapabilityItem.Styles.COMMON, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.IDLE, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.WALK, Animations.BIPED_WALK_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.CHASE, Animations.BIPED_WALK_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.RUN, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.SNEAK, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.KNEEL, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.JUMP, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.SWIM, Animations.BIPED_HOLD_LIECHTENAUER)
                    .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD)
                    .livingMotionModifier(CapabilityItem.Styles.OCHS, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);

    public static final Function<Item, CapabilityItem.Builder> S_SWORD = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(StarWeaponCategory.S_SWORD)
                .styleProvider((playerpatch) -> playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == StarWeaponCategory.S_SWORD? CapabilityItem.Styles.TWO_HAND : CapabilityItem.Styles.ONE_HAND)
                .collider(ColliderPreset.SWORD)
                .newStyleCombo(CapabilityItem.Styles.ONE_HAND, StarAnimations.SWORD_ONEHAND_AUTO1, StarAnimations.SWORD_ONEHAND_AUTO2, StarAnimations.SWORD_ONEHAND_AUTO3, StarAnimations.SWORD_ONEHAND_AUTO4, StarAnimations.SWORD_ONEHAND_DASH, Animations.SWORD_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND, Animations.SWORD_DUAL_AUTO1, Animations.SWORD_DUAL_AUTO2,Animations.DAGGER_DUAL_AUTO3, Animations.SWORD_DUAL_AUTO3, Animations.DAGGER_DUAL_AUTO4,  Animations.SWORD_DUAL_DASH, Animations.SWORD_DUAL_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                .innateSkill(CapabilityItem.Styles.ONE_HAND, (itemstack) -> StarSkill.SWORD_SLASH)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> StarSkill.DUAL_SLASH)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.BLOCK, Animations.SWORD_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.WALK, Animations.BIPED_WALK_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_LONGSWORD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SWORD_DUAL_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_DUAL)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_DUAL_WEAPON)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_DUAL_WEAPON)
                .weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == StarWeaponCategory.S_SWORD);

        if (item instanceof TieredItem tieredItem) {
            builder.hitSound(tieredItem.getTier() == Tiers.WOOD ? EpicFightSounds.BLUNT_HIT : EpicFightSounds.BLADE_HIT);
            builder.hitParticle(tieredItem.getTier() == Tiers.WOOD ? EpicFightParticles.HIT_BLUNT.get() : EpicFightParticles.HIT_BLADE.get());
        }

        return builder;
    };

    public static final Function<Item, CapabilityItem.Builder> S_TACHI = (item) ->
            WeaponCapability.builder()
                    .category(StarWeaponCategory.S_TACHI)
                    .styleProvider((playerpatch) -> CapabilityItem.Styles.TWO_HAND)
                    .collider(ColliderPreset.TACHI)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .canBePlacedOffhand(false)
                    .newStyleCombo(CapabilityItem.Styles.TWO_HAND, StarAnimations.LONGSWORD_OLD_AUTO1, StarAnimations.LONGSWORD_OLD_AUTO2, StarAnimations.LONGSWORD_OLD_AUTO3, StarAnimations.LONGSWORD_OLD_AUTO4, StarAnimations.LONGSWORD_OLD_DASH,StarAnimations.LONGSWORD_OLD_AIRSLASH)
                    .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                    .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> StarSkill.LETHAL_SLICING)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_TACHI)
                    .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);

    public static final Function<Item, CapabilityItem.Builder> S_GREATSWORD = (item) ->
            WeaponCapability.builder()
                    .category(StarWeaponCategory.S_GREATSWORD)
                    .styleProvider((playerpatch) -> CapabilityItem.Styles.TWO_HAND)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .swingSound(EpicFightSounds.WHOOSH_BIG)
                    .canBePlacedOffhand(false)
                    .collider(ColliderPreset.GREATSWORD)
                    .weaponCombinationPredicator((entitypatch) -> EpicFightCapabilities.getItemStackCapability(entitypatch.getOriginal().getOffhandItem()).getWeaponCategory() == StarWeaponCategory.S_GREATSWORD)
                    .newStyleCombo(Styles.TWO_HAND, StarAnimations.GREATSWORD_OLD_AUTO1, StarAnimations.GREATSWORD_OLD_AUTO2, StarAnimations.GREATSWORD_OLD_AUTO3, StarAnimations.GREATSWORD_OLD_DASH, StarAnimations.GREATSWORD_OLD_AIRSLASH)
                    .innateSkill(Styles.TWO_HAND, (itemstack) -> StarSkill.WIND_SLASH)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, StarAnimations.GREATSWORD_OLD_IDLE)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, StarAnimations.GREATSWORD_OLD_WALK)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, StarAnimations.GREATSWORD_OLD_RUN)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_GREATSWORD)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.GREATSWORD_GUARD);



    public static final Function<Item, CapabilityItem.Builder> S_SPEAR = (item) ->
            WeaponCapability.builder()
                    .category(StarWeaponCategory.S_SPEAR)
                    .styleProvider((playerpatch) -> (playerpatch.getHoldingItemCapability(InteractionHand.OFF_HAND).getWeaponCategory() == WeaponCategories.SHIELD) ?
                            Styles.ONE_HAND : Styles.TWO_HAND)
                    .collider(ColliderPreset.SPEAR)
                    .hitSound(EpicFightSounds.BLADE_HIT)
                    .canBePlacedOffhand(false)
                    .newStyleCombo(Styles.ONE_HAND, StarAnimations.SSPEAR_ONEHAND_AUTO, StarAnimations.SSPEAR_DASH, Animations.SPEAR_ONEHAND_AIR_SLASH)
                    .newStyleCombo(Styles.TWO_HAND, StarAnimations.SSPEAR_TWOHAND_AUTO1, StarAnimations.SSPEAR_TWOHAND_AUTO2, StarAnimations.SSPEAR_DASH, Animations.SPEAR_TWOHAND_AIR_SLASH)
                    .newStyleCombo(Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
                    .innateSkill(Styles.ONE_HAND, (itemstack) -> StarSkill.HEARTPIERCERS)
                    .innateSkill(Styles.TWO_HAND, (itemstack) -> StarSkill.SPEAR_SLASH)
                    .livingMotionModifier(Styles.ONE_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                    .livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.SPEAR_GUARD);

    public static final Function<Item, CapabilityItem.Builder> KATANA = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(StarWeaponCategory.KATANA)
                .styleProvider((entitypatch) -> {
                    if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                        if (playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(BattojutsuPassive.SHEATH) &&
                                playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(BattojutsuPassive.SHEATH)) {
                            return Styles.SHEATH;
                        }
                    }
                    return Styles.TWO_HAND;
                })
                .passiveSkill(EpicFightSkills.BATTOJUTSU_PASSIVE)
                .hitSound(EpicFightSounds.BLADE_HIT)
                .collider(ColliderPreset.UCHIGATANA)
                .canBePlacedOffhand(false)
                .newStyleCombo(Styles.SHEATH, StarAnimations.KATANA_SHEATHING_AUTO, StarAnimations.KATANA_SHEATHING_DASH, StarAnimations.KATANA_SHEATH_AIR_SLASH)
                .newStyleCombo(Styles.TWO_HAND, StarAnimations.KATANA_AUTO1, StarAnimations.KATANA_AUTO2, StarAnimations.KATANA_AUTO3,  StarAnimations.YAMATO_DASH, StarAnimations.KATANA_AIR_SLASH)
                .newStyleCombo(Styles.MOUNT, Animations.SWORD_MOUNT_ATTACK)
                .innateSkill(Styles.SHEATH, (itemstack) -> EpicFightSkills.BATTOJUTSU)
                .innateSkill(Styles.TWO_HAND, (itemstack) -> StarSkill.FATAL_DRAW)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, StarAnimations.WALK_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, StarAnimations.WALK_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, StarAnimations.RUN_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, StarAnimations.WALK_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLOAT, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.FALL, StarAnimations.BIPED_HOLD_KATANA)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.IDLE, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.KNEEL, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.WALK, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.CHASE, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.RUN, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.SNEAK, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.SWIM, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.FLOAT, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.SHEATH, LivingMotions.FALL, StarAnimations.BIPED_HOLD_KATANA_SHEATHING)
                .livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.UCHIGATANA_GUARD);

        return builder;
    };

    public static final Function<Item, CapabilityItem.Builder> SLASH = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder().category(WeaponCategories.TACHI).styleProvider((playerpatch) -> {
            return Styles.TWO_HAND;
        }).collider(ColliderPreset.TACHI).swingSound(EpicFightSounds.WHOOSH).hitSound(EpicFightSounds.BLADE_HIT).canBePlacedOffhand(false).newStyleCombo(Styles.TWO_HAND, new StaticAnimation[]{Animations.LONGSWORD_LIECHTENAUER_AUTO1, Animations.TACHI_AUTO2, Animations.TACHI_AUTO3, Animations.TACHI_DASH, Animations.LONGSWORD_AIR_SLASH}).innateSkill(Styles.TWO_HAND, (itemstack) -> {
            return StarSkill.SLASH;
        }).livingMotionModifier(Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_LONGSWORD).livingMotionModifier(Styles.TWO_HAND, LivingMotions.JUMP, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.FLY, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_FLY, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.CREATIVE_IDLE, Animations.BIPED_HOLD_TACHI).livingMotionModifier(Styles.TWO_HAND, LivingMotions.BLOCK, Animations.LONGSWORD_GUARD);
        return builder;
    };
    private static final Map<String, Function<Item, CapabilityItem.Builder>> PRESETS = Maps.newHashMap();

    public StarWeaponCapabilityPresets() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put("wusong", WUSONG);
        event.getTypeEntry().put("uchigatana1", UCHIGATANA_1);
        event.getTypeEntry().put("tachi1", TACHI_1);
        event.getTypeEntry().put("tachi2", TACHI_2);
        event.getTypeEntry().put("stormruler", STORM_RULER);
        event.getTypeEntry().put("sword1", SWORD_1);
        event.getTypeEntry().put("zwei", ZWEI);
        event.getTypeEntry().put("dragonslayer", DRAGONSLAYER);
        event.getTypeEntry().put("slash", SLASH);
        event.getTypeEntry().put("yamato", YAMATO);
        event.getTypeEntry().put("katana", KATANA);
        event.getTypeEntry().put("s_longsword", S_LONGSWORD);
        event.getTypeEntry().put("s_sword", S_SWORD);
        event.getTypeEntry().put("s_tachi", S_TACHI);
        event.getTypeEntry().put("s_greatsword", S_GREATSWORD);
        event.getTypeEntry().put("s_spear", S_SPEAR);
    }

    public static Function<Item, CapabilityItem.Builder> get(String typeName) {
        ResourceLocation rl = new ResourceLocation(typeName);
        if (!PRESETS.containsKey(rl.getPath())) {
            throw new IllegalArgumentException("Can't find weapon type: " + typeName);
        } else {
            return (Function)PRESETS.get(rl.getPath());
        }
    }
}