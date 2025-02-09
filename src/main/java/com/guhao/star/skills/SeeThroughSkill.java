package com.guhao.star.skills;

import com.guhao.star.regirster.Keys;
import com.guhao.star.units.Guard_Array;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult.ResultType;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.skill.SkillDataManager.ValueType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.UUID;

public class SeeThroughSkill extends Skill {
    private static final int active = 60;
    private static final UUID EVENT_UUID = UUID.fromString("31a396ea-0361-11ee-be56-0242ac114514");
    public static final SkillDataManager.SkillDataKey<Boolean> CUT;
    public static SkillDataManager.SkillDataKey<Boolean> IS_CUT_DOWN = SkillDataKey.createDataKey(ValueType.BOOLEAN);
    public static final SkillDataManager.SkillDataKey<Boolean> LEG;
    public static final SkillDataManager.SkillDataKey<Integer> ACTIVE_TIME;

    public SeeThroughSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    public static Skill.Builder createSeeThroughSkillBuilder() {
        return (new Skill.Builder()).setCategory(SkillCategories.IDENTITY).setActivateType(ActivateType.DURATION).setResource(Resource.NONE);
    }

    /**
     * 客户端要用的就加一下，保险
     */
    public static void register(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            IS_CUT_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        });
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(CUT);
        container.getDataManager().registerData(LEG);
        container.getDataManager().registerData(ACTIVE_TIME);
        container.getDataManager().registerData(IS_CUT_DOWN);
        container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> ep = (LivingEntityPatch)EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), LivingEntityPatch.class);
            if (ep != null && event.isParried()) {
                DynamicAnimation patt2326$temp = ep.getAnimator().getPlayerFor(null).getAnimation();
                if (patt2326$temp instanceof StaticAnimation) {
                    StaticAnimation animation = (StaticAnimation)patt2326$temp;
                    if (Guard_Array.isNoParry(animation)) {
                        container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.GLOWING, active, active, false, false));
                        container.getDataManager().setData(CUT, true);
                        container.getDataManager().setData(ACTIVE_TIME, 0);
                        container.getDataManager().setData(LEG, false);
                    }
                }
            }

            if (event.getResult() == ResultType.SUCCESS) {
                container.getDataManager().setData(ACTIVE_TIME, active);
            }

        });
        container.getExecuter().getEventListener().addEventListener(EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> ep = (LivingEntityPatch)EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), LivingEntityPatch.class);
            if (ep != null) {
                DynamicAnimation patt3283$temp = ep.getAnimator().getPlayerFor(null).getAnimation();
                if (patt3283$temp instanceof StaticAnimation) {
                    StaticAnimation animation = (StaticAnimation)patt3283$temp;
                    if (Guard_Array.canDodge(animation)) {
                        container.getExecuter().getOriginal().addEffect(new MobEffectInstance(MobEffects.GLOWING, active, active, false, false));
                        container.getDataManager().setData(LEG, true);
                        container.getDataManager().setData(ACTIVE_TIME, 0);
                        container.getDataManager().setData(CUT, false);
                    }
                }
            }

        });
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
    }

    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if(container.getExecuter().isLogicalClient()){
            container.getDataManager().setDataSync(IS_CUT_DOWN,  Keys.CUT.isDown(), ((LocalPlayer) container.getExecuter().getOriginal()));
        }
        if (container.getDataManager().getDataValue(ACTIVE_TIME) == null) {
            container.getDataManager().setData(ACTIVE_TIME, active);
        }

        if (container.getDataManager().getDataValue(CUT) == null) {
            container.getDataManager().setData(CUT, false);
        }

        if (container.getDataManager().getDataValue(LEG) == null) {
            container.getDataManager().setData(LEG, false);
        }

        if (container.getDataManager().getDataValue(ACTIVE_TIME) >= active) {
            container.getDataManager().setData(CUT, false);
            container.getDataManager().setData(LEG, false);
            container.getExecuter().getOriginal().removeEffect(MobEffects.GLOWING);
        }

        if (container.getDataManager().getDataValue(ACTIVE_TIME) < active) {
            container.getDataManager().setData(ACTIVE_TIME, container.getDataManager().getDataValue(ACTIVE_TIME) + 1);
        }

        boolean isCut = container.getDataManager().getDataValue(CUT);
        boolean isLeg = container.getDataManager().getDataValue(LEG);
        if (isLeg && container.getDataManager().getDataValue(IS_CUT_DOWN) && !(container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation)) {
            container.getExecuter().playAnimationSynchronized(Animations.REVELATION_TWOHAND, 0.0F);
            container.getExecuter().getOriginal().removeEffect(MobEffects.GLOWING);
            container.getDataManager().setData(LEG, false);
            container.getDataManager().setData(ACTIVE_TIME, active);
        }

        if (isCut && container.getDataManager().getDataValue(IS_CUT_DOWN) && !(container.getExecuter().getAnimator().getPlayerFor(null).getAnimation() instanceof AttackAnimation)) {
            container.getExecuter().playAnimationSynchronized(Animations.RUSHING_TEMPO2, 0.0F);
            container.getExecuter().getOriginal().removeEffect(MobEffects.GLOWING);
            container.getDataManager().setData(CUT, false);
            container.getDataManager().setData(ACTIVE_TIME, active);
        }

    }

    static {
        CUT = SkillDataKey.createDataKey(ValueType.BOOLEAN);
        LEG = SkillDataKey.createDataKey(ValueType.BOOLEAN);
        ACTIVE_TIME = SkillDataKey.createDataKey(ValueType.INTEGER);
    }
}