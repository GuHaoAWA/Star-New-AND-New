package com.guhao.star.event;

import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber
public class LevelEvent {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.world);
        }
    }

    public static void execute(LevelAccessor world) {
        execute(null, world);
    }

    private static void execute(@Nullable Event event, LevelAccessor world) {
        Random random = new Random();
        if (!world.isClientSide() && world.getServer() != null && random.nextDouble(0.0,1.0) < 0.0000001)
            world.getServer().getPlayerList().broadcastMessage(new TextComponent("\u00A7eHerobrine \u52A0\u5165\u4E86\u6E38\u620F"), ChatType.SYSTEM, Util.NIL_UUID);
    }
}