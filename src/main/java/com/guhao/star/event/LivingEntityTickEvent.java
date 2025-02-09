package com.guhao.star.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.particle.EpicFightParticles;

import javax.annotation.Nullable;

import static com.github.alexthe666.alexsmobs.effect.AMEffectRegistry.EXSANGUINATION;

@Mod.EventBusSubscriber
public class LivingEntityTickEvent {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        execute(event, event.getEntityLiving());
    }

    public static void execute(LivingEntity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, LivingEntity entity) {
        if (entity == null)
            return;
        if (entity.getLevel() instanceof ServerLevel serverLevel && entity.hasEffect(new MobEffectInstance(EXSANGUINATION).getEffect())) serverLevel.sendParticles(EpicFightParticles.BLOOD.get(), (entity.getX()), (entity.getEyeY() - 1), (entity.getZ()), 4, 0.1, 0.1, 0.1, 0.3);

    }

}
