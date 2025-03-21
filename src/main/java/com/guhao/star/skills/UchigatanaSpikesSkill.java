package com.guhao.star.skills;

import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.ConditionalWeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class UchigatanaSpikesSkill extends ConditionalWeaponInnateSkill {
	public UchigatanaSpikesSkill(Builder builder) {
		super(builder);
	}
	
	@Override
	public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
		boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(BattojutsuPassive.SHEATH);
		
		if (isSheathed) {
			executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		} else {
			executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		}
		
		super.executeOnServer(executer, args);
	}
}