package com.guhao.star.units;

import com.dfdyz.epicacg.registry.WeaponTypes;
import com.guhao.GuHaoAnimations;
import com.guhao.star.efmex.StarAnimations;
import net.minecraft.world.damagesource.DamageSource;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Guard_Array() {

    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    static final StaticAnimation[] DODGE;
    static final StaticAnimation[] CANDODGE;
    static final CapabilityItem.WeaponCategories[] AVAILABLE_WEAPON_TYPES;
    static final WeaponTypes.EpicACGWeaponCategories[] AVAILABLE_WEAPON_TYPES2;
    //static final StaticAnimation[] EXECUTE;
    static final List<StaticAnimation> EXECUTE = new ArrayList<>();
    static {//无视格挡red
         GUARD = new StaticAnimation[]{
                 Animations.TSUNAMI_REINFORCED,
                 Animations.WRATHFUL_LIGHTING,
                 Animations.REVELATION_TWOHAND,
                 Animations.BATTOJUTSU_DASH,
                 StarAnimations.LETHAL_SLICING_ONCE1,
                 StarAnimations.KATANA_SHEATH_DASH,
                 StarAnimations.SCRATCH,
                 StarAnimations.KILL,
                 StarAnimations.EVIL_BLADE,
                 WOMAnimations.TORMENT_AUTO_1,
                 WOMAnimations.RUINE_DASH,
                 WOMAnimations.SOLAR_QUEMADURA,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                 WOMAnimations.GESETZ_SPRENGKOPF,
                 WOMAnimations.SOLAR_AUTO_2_POLVORA,
                 WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
                 WOMAnimations.SOLAR_AUTO_2_POLVORA,

////////////////////////////////////////////////////////////////
                 GuHaoAnimations.NB_ATTACK,
                 GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                 GuHaoAnimations.BIU,
                 GuHaoAnimations.GUHAO_BIU,
                 GuHaoAnimations.BLOOD_JUDGEMENT
         };//只能完美org
         PARRY = new StaticAnimation[]{
                 Animations.UCHIGATANA_DASH,
                 Animations.TACHI_DASH,
                 Animations.SPEAR_DASH,
                 Animations.LONGSWORD_DASH,
                 Animations.REVELATION_ONEHAND,
                 StarAnimations.BLADE_RUSH_FINISHER,
                 StarAnimations.LONGSWORD_OLD_DASH,
                 StarAnimations.ZWEI_DASH,
                 StarAnimations.SSPEAR_DASH,
                 WOMAnimations.HERRSCHER_AUTO_2,
                 WOMAnimations.STAFF_KINKONG,
                 WOMAnimations.SOLAR_HORNO,
                 WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                 WOMAnimations.GESETZ_AUTO_3,
                 WOMAnimations.RUINE_REDEMPTION,
                 WOMAnimations.RUINE_COMET,
                 WOMAnimations.AGONY_AUTO_1,
                 WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4
         };//purple
        DODGE = new StaticAnimation[]{
                StarAnimations.LETHAL_SLICING_ONCE1,
                StarAnimations.KATANA_SHEATH_DASH,
                StarAnimations.KILL,
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.RUINE_DASH,
                WOMAnimations.SOLAR_QUEMADURA,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
        };//see_through
        CANDODGE = new StaticAnimation[]{
                Animations.UCHIGATANA_DASH,
                Animations.TACHI_DASH,
                Animations.SPEAR_DASH,
                Animations.LONGSWORD_DASH,
                Animations.REVELATION_ONEHAND,
                StarAnimations.BLADE_RUSH_FINISHER,
                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.SOLAR_HORNO,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.GESETZ_AUTO_3,
                WOMAnimations.RUINE_REDEMPTION,
                WOMAnimations.SOLAR_AUTO_2_POLVORA,

                Animations.TSUNAMI_REINFORCED,
                Animations.WRATHFUL_LIGHTING,
                Animations.REVELATION_TWOHAND,
                WOMAnimations.GESETZ_SPRENGKOPF,
        };
        AVAILABLE_WEAPON_TYPES = new CapabilityItem.WeaponCategories[]{CapabilityItem.WeaponCategories.UCHIGATANA
                , CapabilityItem.WeaponCategories.SWORD
                , CapabilityItem.WeaponCategories.DAGGER
                , CapabilityItem.WeaponCategories.AXE
                , CapabilityItem.WeaponCategories.TRIDENT
                , CapabilityItem.WeaponCategories.FIST};
        AVAILABLE_WEAPON_TYPES2 = new WeaponTypes.EpicACGWeaponCategories[]{
                WeaponTypes.EpicACGWeaponCategories.SCYTHE
        };
         EXECUTE.add(Animations.BIPED_KNEEL);
         EXECUTE.add(Animations.WITHER_NEUTRALIZED);
         EXECUTE.add(Animations.VEX_NEUTRALIZED);
         EXECUTE.add(Animations.SPIDER_NEUTRALIZED);
         EXECUTE.add(Animations.DRAGON_NEUTRALIZED);
         EXECUTE.add(Animations.ENDERMAN_NEUTRALIZED);
         EXECUTE.add(Animations.BIPED_COMMON_NEUTRALIZED);
         EXECUTE.add(Animations.GREATSWORD_GUARD_BREAK);
    }

    public StaticAnimation[] getGuard() {
        return GUARD;
    }
    public StaticAnimation[] getParry() {
        return PARRY;
    }
    public List<StaticAnimation> getExecute() {
        return EXECUTE;
    }
    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
    public static boolean isNoGuard(StaticAnimation staticAnimation) {
        return Arrays.asList(GUARD).contains(staticAnimation);
    }
    public static boolean isNoParry(StaticAnimation staticAnimation) {
        return Arrays.asList(PARRY).contains(staticAnimation);
    }
    public static boolean isNoDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(DODGE).contains(staticAnimation);
    }
    public static boolean canDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(CANDODGE).contains(staticAnimation);
    }
    public static boolean isChargingSkill(WeaponCategory weaponCategories) {
        return (Arrays.asList(AVAILABLE_WEAPON_TYPES).contains(weaponCategories) || Arrays.asList(AVAILABLE_WEAPON_TYPES2).contains(weaponCategories));
    }
}