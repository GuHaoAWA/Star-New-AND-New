package com.guhao.star.regirster;


import com.guhao.star.client.particle.par.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.star.Star.MODID;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleType {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES;
    public static final RegistryObject<SimpleParticleType> DANGER;
    public static final RegistryObject<SimpleParticleType> DANGER_RED;
    public static final RegistryObject<SimpleParticleType> DANGER_BLACK;
    public static final RegistryObject<SimpleParticleType> DANGER_BLUE;
    public static final RegistryObject<SimpleParticleType> DING;
    public static final RegistryObject<SimpleParticleType> FIRE_BALL;
    public static final RegistryObject<SimpleParticleType> CAI;
    public static final RegistryObject<SimpleParticleType> EX_LASER;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void RP(ParticleFactoryRegisterEvent event) {
        ParticleEngine PE = Minecraft.getInstance().particleEngine;
        PE.register(DANGER.get(), Dangers.DangerParticleProvider::new);
        PE.register(DANGER_RED.get(), Dangers_Red.Dangers_RedParticleProvider::new);
        PE.register(DANGER_BLACK.get(), Dangers_Black.Dangers_BlackParticleProvider::new);
        PE.register(DANGER_BLUE.get(), Dangers_Blue.Dangers_BlueParticleProvider::new);
        PE.register(DING.get(), Ding.DangerParticleProvider::new);
        PE.register(CAI.get(), Cai.CaiParticleProvider::new);
        PE.register(FIRE_BALL.get(), Fire_Ball.Provider::new);
        PE.register(EX_LASER.get(), EX_Laser.Provider::new);
    }

    public ParticleType() {
    }

    static {
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
        DANGER = PARTICLES.register("dangers", () -> new SimpleParticleType(true));
        DANGER_RED = PARTICLES.register("dangers_red", () -> new SimpleParticleType(true));
        DANGER_BLACK = PARTICLES.register("dangers_black", () -> new SimpleParticleType(true));
        DANGER_BLUE = PARTICLES.register("dangers_blue", () -> new SimpleParticleType(true));
        FIRE_BALL = PARTICLES.register("fire_ball", () -> new SimpleParticleType(true));
        DING = PARTICLES.register("ding", () -> new SimpleParticleType(true));
        CAI = PARTICLES.register("cai", () -> new SimpleParticleType(true));
        EX_LASER = PARTICLES.register("ex_laser", () -> new SimpleParticleType(true));
    }
}
