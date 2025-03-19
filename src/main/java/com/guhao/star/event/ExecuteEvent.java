package com.guhao.star.event;

import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.network.server.NetworkManager;
import com.guhao.star.network.server.SPLockOn;
import com.guhao.star.regirster.Sounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.LongHitAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


@Mod.EventBusSubscriber
public class Execute {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickEntity(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != event.getPlayer().getUsedItemHand()) return;
        execute(event.getPlayer(), event.getEntity().level);
    }

    public static void execute(Player player, Level level) {
        if (player == null || level == null || level.isClientSide()) return;
        Vec3 playerPosition = new Vec3(player.getX(), player.getEyeY(), player.getZ());
        getNearbyEntities(level, playerPosition).forEach(targetEntity -> {
            LivingEntityPatch<?> targetPatch = EpicFightCapabilities.getEntityPatch(targetEntity, LivingEntityPatch.class);
            if (targetPatch != null && targetEntity != player) {
                PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
                if (isAnimationValid(targetPatch, playerPatch)) {
                    handleExecution((ServerPlayer) player, targetPatch, playerPatch);
                }
            }
        });
    }

    private static List<LivingEntity> getNearbyEntities(Level level, Vec3 playerPosition) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(playerPosition, playerPosition).inflate(3), e -> true)
                .stream()
                .sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(playerPosition)))
                .limit(2)
                .toList();
    }

    private static boolean isAnimationValid(LivingEntityPatch<?> targetPatch, PlayerPatch<?> playerPatch) {
        return (targetPatch.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && staticAnimation == Animations.BIPED_KNEEL) ||
                (targetPatch.getAnimator().getPlayerFor(null).getAnimation() instanceof LongHitAnimation longHitAnimation &&
                        Set.of(Animations.WITHER_NEUTRALIZED, Animations.VEX_NEUTRALIZED, Animations.SPIDER_NEUTRALIZED,
                                Animations.DRAGON_NEUTRALIZED, Animations.ENDERMAN_NEUTRALIZED,
                                Animations.BIPED_COMMON_NEUTRALIZED, Animations.GREATSWORD_GUARD_BREAK).contains(longHitAnimation));
    }

    private static void handleExecution(ServerPlayer player, LivingEntityPatch<?> targetPatch, PlayerPatch<?> playerPatch) {
        Vec3 viewVec = targetPatch.getOriginal().getViewVector(1.0F);
        playerPatch.setGrapplingTarget(targetPatch.getOriginal());
        SPLockOn msg = new SPLockOn();
        NetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg, player);
        player.addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 100, 0));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 70, 50));
        targetPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 40, 0));
        targetPatch.playSound(Sounds.SEKIRO, 1.0F, 1.0F);
      if (playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.SPEAR ) {
            player.teleportTo(targetPatch.getOriginal().getX() + viewVec.x() * 2.0, targetPatch.getOriginal().getY(), targetPatch.getOriginal().getZ() + viewVec.z() * 2.0);
            playerPatch.playAnimationSynchronized(StarAnimations.SPEAR_EXECUTE, 0.0F);
            targetPatch.playAnimationSynchronized(StarAnimations.SPEAR_EXECUTED, 0.25F, SPPlayAnimation::new);
        } else if (playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.GREATSWORD ) {
            player.teleportTo(targetPatch.getOriginal().getX() + viewVec.x() * 1.5, targetPatch.getOriginal().getY(), targetPatch.getOriginal().getZ() + viewVec.z() * 1.5);
            playerPatch.playAnimationSynchronized(StarAnimations.GREATSWORD_EXECUTE, 0.0F);
            targetPatch.playAnimationSynchronized(StarAnimations.GREATSWORD_EXECUTED, 0.8F, SPPlayAnimation::new);
        } else if (playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.DAGGER ) {
            player.teleportTo(targetPatch.getOriginal().getX() + viewVec.x(), targetPatch.getOriginal().getY(), targetPatch.getOriginal().getZ() + viewVec.z());
            playerPatch.playAnimationSynchronized(StarAnimations.DAGGER_EXECUTE, 0.0F);
            targetPatch.playAnimationSynchronized(StarAnimations.DAGGER_EXECUTED, 0.25F, SPPlayAnimation::new);
        } else if (playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory() == CapabilityItem.WeaponCategories.TACHI ) {
            player.teleportTo(targetPatch.getOriginal().getX() + viewVec.x() * 2.0, targetPatch.getOriginal().getY(), targetPatch.getOriginal().getZ() + viewVec.z() * 2.0);
            playerPatch.playAnimationSynchronized(StarAnimations.TACHI_EXECUTE, 0.0F);
            targetPatch.playAnimationSynchronized(StarAnimations.TACHI_EXECUTED, 0.0F, SPPlayAnimation::new);
        } else {
            player.teleportTo(targetPatch.getOriginal().getX() + viewVec.x() * 1.5, targetPatch.getOriginal().getY(), targetPatch.getOriginal().getZ() + viewVec.z() * 1.5);
            playerPatch.playAnimationSynchronized(StarAnimations.EXECUTE_WEAPON, 0.0F);
            targetPatch.playAnimationSynchronized(StarAnimations.EXECUTED_WEAPON, 0.25F, SPPlayAnimation::new);
        }
    }
}


