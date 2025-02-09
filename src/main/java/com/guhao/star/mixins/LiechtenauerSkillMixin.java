package com.guhao.star.mixins;

import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.LiechtenauerSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

@Mixin(value = LiechtenauerSkill.class,remap = false)
public abstract class LiechtenauerSkillMixin extends WeaponInnateSkill {
    @Shadow
    private static final UUID EVENT_UUID = UUID.fromString("244c57c0-a837-11eb-bcbc-0242ac130002");

    @Shadow
    private static boolean isBlockableSource(DamageSource damageSource) {
        return !damageSource.isBypassInvul() && !damageSource.isExplosion();
    }

    public LiechtenauerSkillMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(method = "onInitiate",at = @At("TAIL"))
    public void onInitiate(SkillContainer container, CallbackInfo ci) {

        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            EpicFightDamageSource EFDamageSource = Guard_Array.getEpicFightDamageSources(event.getDamageSource());
            int phaseLevel = event.getPlayerPatch().getEntityState().getLevel();
            if ((EFDamageSource != null)) {
                StaticAnimation an = EFDamageSource.getAnimation();
                if (Guard_Array.isNoGuard(an) || Guard_Array.isNoDodge(an)) return;
            }

            if (event.getAmount() > 0.0F && container.isActivated() && !container.isDisabled() && phaseLevel > 0 && phaseLevel < 3 &&
                    this.canExecute(event.getPlayerPatch()) && isBlockableSource(event.getDamageSource())) {

                DamageSource damageSource = event.getDamageSource();
                boolean isFront = false;
                Vec3 sourceLocation = damageSource.getSourcePosition();

                if (sourceLocation != null) {
                    Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
                    Vec3 toSourceLocation = sourceLocation.subtract(event.getPlayerPatch().getOriginal().position()).normalize();

                    if (toSourceLocation.dot(viewVector) > 0.0D) {
                        isFront = true;
                    }
                }

                if (isFront) {
                    event.getPlayerPatch().playSound(Sounds.BIGBONG, 0.7f,-0.31F, -0.27F);
                    ServerPlayer playerentity = event.getPlayerPatch().getOriginal();
                    EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(playerentity.getLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, playerentity, damageSource.getDirectEntity());

                    float knockback = 0.25F;

                    if (damageSource instanceof EpicFightDamageSource epicfightSource) {
                        knockback += Math.min(epicfightSource.getImpact() * 0.1F, 1.0F);
                    }

                    if (damageSource.getDirectEntity() instanceof LivingEntity livingentity) {
                        knockback += EnchantmentHelper.getKnockbackBonus(livingentity) * 0.1F;
                    }

                    LivingEntityPatch<?> attackerpatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), LivingEntityPatch.class);

                    if (attackerpatch != null) {
                        attackerpatch.setLastAttackEntity(event.getPlayerPatch().getOriginal());
                    }

                    event.getPlayerPatch().knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                    event.setCanceled(true);
                    event.setResult(AttackResult.ResultType.BLOCKED);
                }

            }
        }, 0);

    }
}
