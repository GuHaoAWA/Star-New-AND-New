package com.guhao.star.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class HurtCancelEvent {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null && event.getSource().getEntity() == event.getEntity()) {
            event.setCanceled(true);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {

    }
}