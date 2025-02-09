package com.guhao.star.skills;

import com.google.common.collect.Maps;
import com.guhao.star.efmex.StarAnimations;
import net.minecraft.network.FriendlyByteBuf;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Map;
import java.util.UUID;

import static com.guhao.star.skills.WuSongPassive.CHECK2;
import static com.guhao.star.skills.WuSongPassive.CHECK3;

public class WuSongSkill extends WeaponInnateSkill {


	public final Map<StaticAnimation, AttackAnimation> comboAnimation = Maps.newHashMap();
	private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc49-b83e-16ae590db349");
	public static SkillDataManager.SkillDataKey<Boolean> IS_CTRL_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);

	public WuSongSkill(Skill.Builder<? extends Skill> builder) {
		super(builder);
		this.comboAnimation.put(Animations.TACHI_AUTO1, (AttackAnimation) Animations.RUSHING_TEMPO1);
		this.comboAnimation.put(Animations.TACHI_AUTO2, (AttackAnimation) Animations.RUSHING_TEMPO2);
		this.comboAnimation.put(Animations.TACHI_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO3);
		this.comboAnimation.put(Animations.RUSHING_TEMPO1, (AttackAnimation) StarAnimations.YAMATO_AUTO1);
		this.comboAnimation.put(Animations.RUSHING_TEMPO2, (AttackAnimation) StarAnimations.YAMATO_AUTO1);
		this.comboAnimation.put(Animations.RUSHING_TEMPO3, (AttackAnimation) StarAnimations.YAMATO_AUTO1);
	}

	@Override
	public WeaponInnateSkill registerPropertiesToAnimation() {
		this.comboAnimation.values().forEach((animation) -> animation.phases[0].addProperties(this.properties.get(0).entrySet()));
		return this;
	}

	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(WuSongPassive.SHEATH);
		if (executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(CHECK2) > 0) {
			executer.playAnimationSynchronized(StarAnimations.AOXUE2, 0.0F);
			executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().setData(CHECK2,0);
			super.executeOnServer(executer, args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 6 && !executer.getOriginal().onGround) {
			executer.playAnimationSynchronized(StarAnimations.AOXUE, 0.0F);
			executer.getSkill(SkillSlots.WEAPON_INNATE).setStack(3);
			super.executeOnServer(executer, args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() == 3) {
			executer.playAnimationSynchronized(StarAnimations.HANGDANG, 0.0F);
			super.executeOnServer(executer, args);
			return;
		}
		if (executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(CHECK3) > 0) {
			executer.playAnimationSynchronized(WOMAnimations.AGONY_AUTO_2, 0.0F);
			executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().setData(CHECK3,0);
			super.executeOnServer(executer, args);
			return;
		}
		if (this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation())) {
			executer.playAnimationSynchronized(this.comboAnimation.get(executer.getAnimator().getPlayerFor(null).getAnimation()), 0.0F);
			super.executeOnServer(executer, args);
			return;
		}
		if (isSheathed) {
			executer.playAnimationSynchronized(Animations.BATTOJUTSU_DASH, -0.694F);
			super.executeOnServer(executer, args);
        } else {
			executer.playAnimationSynchronized(Animations.BATTOJUTSU_DASH, 0.0F);
			super.executeOnServer(executer, args);
        }
    }
}