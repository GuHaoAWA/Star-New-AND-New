package com.guhao.star.mixins;

import com.guhao.init.Items;
import com.guhao.star.skills.StarSkill;
import com.guhao.star.units.Guard_Array;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(value = ServerPlayerPatch.class,remap = false)
public abstract class ServerPlayerPatchMixin extends PlayerPatch<ServerPlayer> {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void gatherDamageDealt(EpicFightDamageSource source, float amount) {
        ServerPlayerPatch spp = EpicFightCapabilities.getEntityPatch(this.original, ServerPlayerPatch.class);
        if (source.isBasicAttack()) {
            SkillContainer container = this.getSkill(SkillSlots.WEAPON_INNATE);
            ItemStack mainHandItem = this.getOriginal().getMainHandItem();
            if (!container.isFull() && !container.isActivated() && container.hasSkill(EpicFightCapabilities.getItemStackCapability(mainHandItem).getInnateSkill(this, mainHandItem))) {
                float value = container.getResource() + amount;
                if (spp.getSkill(StarSkill.HYPER_CHARGE_SKILL) != null && (Guard_Array.isChargingSkill(spp.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory()) || spp.getOriginal().getMainHandItem().getItem() == Items.GUHAO.get())) {
                    value = (float) (container.getResource() + amount * (0.96F + spp.getOriginal().getAttributeValue(Attributes.LUCK) * 0.04F));
                }
                if (value > 0.0F) {
                    this.getSkill(SkillSlots.WEAPON_INNATE).getSkill().setConsumptionSynchronize(spp, value);
                }
            }
        }
    }
}
