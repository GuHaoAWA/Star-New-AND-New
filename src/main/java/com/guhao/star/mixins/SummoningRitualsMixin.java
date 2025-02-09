package com.guhao.star.mixins;

import com.almostreliable.summoningrituals.SummoningRituals;
import com.almostreliable.summoningrituals.altar.AltarBlock;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = SummoningRituals.class,remap = false,priority = 9999999)
public class SummoningRitualsMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            PlayerPatch<?> playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            if (!playerpatch.isBattleMode()) {
                if (player.isShiftKeyDown() == item.isEmpty() && block instanceof AltarBlock) {
                    event.setUseBlock(Event.Result.ALLOW);
                    event.setUseItem(Event.Result.DENY);
                    event.setCanceled(false);
                }
            } else {
                event.setCanceled(true);
            }
        }
    }
}
