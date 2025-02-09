package com.guhao.star.event;

import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Items;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlayerTickEvent {
    private static final UUID EVENT_UUID = UUID.fromString("f082557a-b2f9-11eb-8529-1249ac130003");
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event, event.player);
        }
    }

    public static void execute(Player p) {
        execute(null, p);
    }

    private static void execute(@Nullable Event event, Player entity) {
        if (entity == null)
            return;
        if (CuriosApi.getCuriosHelper().findFirstCurio(entity, Items.PERSONA.get()).isPresent()) {
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20, 1));
        }
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
        if (pp != null) {
            pp.getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (e) -> {
                LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(e.getDamageSource().getDirectEntity(), LivingEntityPatch.class);
                if (!entity.isCreative() && ep != null && Guard_Array.getEpicFightDamageSources(e.getDamageSource()) != null && Guard_Array.getEpicFightDamageSources(e.getDamageSource()).getAnimation() == StarAnimations.SCRATCH && e.getResult() == AttackResult.ResultType.SUCCESS) {
                    ep.playSound(Sounds.SEKIRO, 1.0f, 1.0f);
                    Vec3 epPosition = ep.getOriginal().position();
                    double distance = 0.85;
                    double offsetX = 0;
                    Vec3 targetPosition = epPosition.add(ep.getOriginal().getLookAngle().x + offsetX * distance, ep.getOriginal().getLookAngle().y * distance, ep.getOriginal().getLookAngle().z * distance);
                    ep.getOriginal().setSpeed(0.0f);
                    entity.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
                    ep.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(entity.getX(), entity.getEyeY() - 0.1, entity.getZ()));
                    pp.getOriginal().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(ep.getOriginal().getX(), ep.getOriginal().getEyeY() - 0.1, ep.getOriginal().getZ()));
                    ep.getOriginal().setDeltaMovement(0, 0, 0);
                    ep.getOriginal().addEffect(new MobEffectInstance(Effect.EXECUTE.get(), 140, 1));
                    ep.playAnimationSynchronized(StarAnimations.KILL, 0.0F);
                    entity.setDeltaMovement(0, 0, 0);
                    entity.addEffect(new MobEffectInstance(Effect.EXECUTED.get(), 140, 1));
                    pp.playAnimationSynchronized(StarAnimations.KILLED, 0.0F);
                }
            });
        }
    }
}
