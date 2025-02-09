package com.guhao.star.event;

import com.guhao.star.efmex.StarAnimations;
import com.p1nero.wukong.epicfight.weapon.WukongWeaponCategories;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class DingActionEvent {
    public static void execute(Player entity) {
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
        if (pp != null && PlayerMagicData.getPlayerMagicData(entity).getMana() >= 100 && pp.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == WukongWeaponCategories.WK_STAFF) {
            pp.playAnimationSynchronized(StarAnimations.DING,0.0f);
            PlayerMagicData.getPlayerMagicData(entity).setMana(PlayerMagicData.getPlayerMagicData(entity).getMana() - 100);
        }
    }
}
