package com.guhao.star.skills;

import com.guhao.star.efmex.StarAnimations;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillDataManager.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.UUID;

public class WuSongPassive extends Skill {
	public static final SkillDataKey<Boolean> SHEATH = SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
	public static final SkillDataKey<Integer> CHECK1 = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> CHECK2 = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
	public static final SkillDataKey<Integer> CHECK3 = SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);

	private static final UUID EVENT_UUID = UUID.fromString("a416c93a-42cb-11eb-b378-0242ac139992");
	
	public WuSongPassive(Builder<? extends Skill> builder) {
		super(builder);
	}
	
	@Override
	public void onInitiate(SkillContainer container) {
		super.onInitiate(container);
		container.getDataManager().registerData(SHEATH);
		container.getDataManager().registerData(CHECK1);
		container.getDataManager().registerData(CHECK2);
		container.getDataManager().registerData(CHECK3);
		container.getExecuter().getEventListener().addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
			container.getSkill().setConsumptionSynchronize(event.getPlayerPatch(), 0.0F);
			container.getSkill().setStackSynchronize(event.getPlayerPatch(), 0);
		});
		
		container.getExecuter().getEventListener().addEventListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
			this.onReset(container);
		});
		container.getExecuter().getEventListener().addEventListener(EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
			if (event.isParried() && event.getResult() == AttackResult.ResultType.BLOCKED) {
				container.getDataManager().setData(CHECK3,20);
			}
		});
		container.getExecuter().getEventListener().addEventListener(EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID, (event) -> {
			if (event.getDamageSource().getAnimation() == StarAnimations.AOXUE) {
				container.getDataManager().setData(CHECK2,20);
			}
			if ((event.getDamageSource().getAnimation() == StarAnimations.AOXUE2)) {
				event.getTarget().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2, false, true));
				event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 4, false, true));
				event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 4, false, true));
			}
		});
	}
	@Override
	public void updateContainer(SkillContainer container) {
		super.updateContainer(container);
		container.getExecuter().getOriginal().addEffect((new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 2, false, false)));
		if (container.getDataManager().getDataValue(SHEATH)) {
			container.getExecuter().getOriginal().addEffect((new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 4, false, false)));
		}
		if ( container.getDataManager().getDataValue(CHECK3) >= 0) container.getDataManager().setData(CHECK3,container.getDataManager().getDataValue(CHECK3) - 1);
		if ( container.getDataManager().getDataValue(CHECK2) >= 0) container.getDataManager().setData(CHECK2,container.getDataManager().getDataValue(CHECK2) - 1);
		if ( container.getDataManager().getDataValue(CHECK1) >= 0) container.getDataManager().setData(CHECK1,container.getDataManager().getDataValue(CHECK1) - 1);
//		if (!container.getExecuter().getOriginal().isSprinting()) {
//			container.getDataManager().setData(CHECK1, container.getDataManager().getDataValue(CHECK1) + 1);
//		} else {
//			container.getDataManager().setData(CHECK1,0);
//			container.getDataManager().setData(CHECK2,0);
//		}
//		if (container.getDataManager().getDataValue(CHECK1) >= 160) {
//			container.getDataManager().setData(CHECK2,container.getDataManager().getDataValue(CHECK2) + 1);
//		}
//		if (container.getDataManager().getDataValue(CHECK2) >= 20) {
//			container.getDataManager().setData(CHECK2,0);
//			container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).setStack(container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getStack() - 1);
//		}
	}
	@Override
	public void onRemoved(SkillContainer container) {
		container.getExecuter().getEventListener().removeListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
		container.getExecuter().getEventListener().removeListener(EventType.HURT_EVENT_PRE, EVENT_UUID);
	}
	
	@Override
	public void onReset(SkillContainer container) {
		PlayerPatch<?> executer = container.getExecuter();
		
		if (!executer.isLogicalClient()) {
			if (container.getDataManager().getDataValue(SHEATH)) {
				ServerPlayerPatch playerpatch = (ServerPlayerPatch)executer;
				container.getDataManager().setDataSync(SHEATH, false, playerpatch.getOriginal());
				playerpatch.modifyLivingMotionByCurrentItem();
				container.getSkill().setConsumptionSynchronize(playerpatch, 0);
			}
		}
	}
	
	@Override
	public void setConsumption(SkillContainer container, float value) {
		PlayerPatch<?> executer = container.getExecuter();
		
		if (!executer.isLogicalClient()) {
			if (container.getMaxResource() < value) {
				ServerPlayer serverPlayer = (ServerPlayer) executer.getOriginal();
				container.getDataManager().setDataSync(SHEATH, true, serverPlayer);
				((ServerPlayerPatch)container.getExecuter()).modifyLivingMotionByCurrentItem();
				SPPlayAnimation msg3 = new SPPlayAnimation(Animations.BIPED_UCHIGATANA_SCRAP, serverPlayer.getId(), 0.0F);
				EpicFightNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg3, serverPlayer);
			}
		}
		
		super.setConsumption(container, value);
	}
	
	@Override
	public boolean shouldDeactivateAutomatically(PlayerPatch<?> executer) {
		return true;
	}
	
	@Override
	public float getCooldownRegenPerSecond(PlayerPatch<?> player) {
		return player.getOriginal().isUsingItem() ? 0.0F : 1.0F;
	}
}