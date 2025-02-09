package com.guhao.star.event;

import com.guhao.star.regirster.Sounds;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Ciallo {
    @SubscribeEvent
    public static void onPlayerTick(ServerChatEvent event) {
        execute(event, event.getPlayer().level, event.getMessage(), event.getPlayer());
    }

    public static void execute(Level world, String message, Player player) {
        execute(null, world, message, player);
    }

    private static void execute(@Nullable Event event, Level world, String message, Player player) {
        if (message.equals("Ciallo") || message.equals("ciallo")) {
            if (!world.isClientSide() && player.getServer() != null) {
                player.getLevel().getServer().getPlayerList().broadcastMessage(new TextComponent("§dCiallo～(∠・ω< )⌒☆"), ChatType.SYSTEM, Util.NIL_UUID);
                for (ServerPlayer serverPlayer : player.getLevel().getServer().getPlayerList().getPlayers()) {
                    // 在每个玩家位置播放音效
                    ServerPlayerPatch pp = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
//                    serverPlayer.playSound(Sounds.CIALLO,1f, 1f);
                    pp.playSound(Sounds.CAILLO,1.0f,1.0f);
                }
            }
        }
    }
}
