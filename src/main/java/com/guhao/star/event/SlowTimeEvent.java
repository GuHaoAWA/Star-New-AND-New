package com.guhao.star.event;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SlowTimeEvent {
//    @SubscribeEvent
//    public static void onEntityJoin(EntityJoinWorldEvent event) {
//        execute(event, event.getEntity());
//    }
//
//    public static void execute(Entity entity) {
//        execute(null, entity);
//    }
//
//    private static void execute(@Nullable Event event, Entity entity) {
//        if (((entity.getLevel().getServer() != null && entity.getLevel().getServer().getPlayerCount() >= 2) || (FMLEnvironment.dist.isDedicatedServer())))
//        {
//        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
//        if (pp != null) {
//            pp.getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, UUID.fromString("32a996ea-0361-11ee-be56-0242ac114514"), (dodge) -> {
//                LivingEntityPatch<?> lep = EpicFightCapabilities.getEntityPatch(dodge.getDamageSource().getDirectEntity(), LivingEntityPatch.class);
//                if (lep != null) {
//                    dodge.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(Effect.SLOW_TIME.get(), 4, 1, false, false));
//                    lep.getOriginal().addEffect(new MobEffectInstance(Effect.SLOW_TIME.get(), 4, 1, false, false));
//                }
//            }, 1);
//        }
//        }
//    }
}