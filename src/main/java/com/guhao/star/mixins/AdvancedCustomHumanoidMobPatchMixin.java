package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import com.nameless.indestructible.api.animation.types.CustomGuardAnimation;
import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.AttackResult.ResultType;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.Random;

@Mixin(
        value = {AdvancedCustomHumanoidMobPatch.class},
        remap = false
)
public abstract class AdvancedCustomHumanoidMobPatchMixin<T extends PathfinderMob> extends HumanoidMobPatch<T> {
    @Mutable
    @Final
    @Shadow
    private final AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider;
    @Shadow
    private boolean cancel_block;
    @Shadow
    private int maxParryTimes;
    @Shadow
    private boolean isParry;
    @Shadow
    private int parryCounter = 0;
    @Shadow
    private int parryTimes = 0;
    @Shadow
    private int stun_immunity_time;
    @Shadow
    private boolean isRunning = false;
    @Shadow
    private float lastGetImpact;

    public AdvancedCustomHumanoidMobPatchMixin(Faction faction, AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider) {
        super(faction);
        this.provider = provider;
    }

    @Shadow
    public boolean canBlockProjectile() {
        return true;
    }

    @Shadow
    public boolean isBlocking() {
        return false;
    }

    @Shadow
    public CustomGuardAnimation getGuardAnimation() {
        return null;
    }

    @Shadow
    public float getParryCostMultiply() {
        return 0.0F;
    }

    @Shadow
    public StaticAnimation getParryAnimation() {
        return null;
    }

    @Shadow
    public float getMaxStamina() {
        return 0.0F;
    }

    @Shadow
    public float getStamina() {
        return 0.0F;
    }

    @Shadow
    public void setStamina(float value) {
    }

    @Shadow
    public void setAttackSpeed(float value) {
    }

    @Shadow
    public float getGuardCostMultiply() {
        return 0.0F;
    }

    @Shadow
    public void setBlocking(boolean blocking) {
    }

    @Shadow
    public StaticAnimation getCounter() {
        return null;
    }

    @Shadow
    public float getCounterChance() {
        return 0.0F;
    }

    @Shadow
    public float getCounterStamina() {
        return 0.0F;
    }

    @Shadow
    public float getCounterSpeed() {
        return 0.0F;
    }

    @Mutable
    @Shadow @Final private float reganShieldMultiply;
    @Shadow
    private boolean canBlockSource(DamageSource damageSource) {
        return !damageSource.isExplosion() && !damageSource.isMagic() && !damageSource.isBypassInvul() && (!damageSource.isProjectile() || this.canBlockProjectile());
    }

    @Inject(
            method = {"setAIAsInfantry"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void setAIAsInfantry(boolean holdingRanedWeapon, CallbackInfo ci) {
        if (this.getOriginal().hasEffect(Effect.EXECUTED.get())) {
            ci.cancel();
        }

    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private AttackResult tryProcess(DamageSource damageSource, float amount) {
        if (Guard_Array.getEpicFightDamageSources(damageSource) != null && Guard_Array.isNoGuard(Guard_Array.getEpicFightDamageSources(damageSource).getAnimation())) {
            return new AttackResult(ResultType.SUCCESS, amount);
        }
        if (this.isBlocking()) {
            CustomGuardAnimation animation = this.getGuardAnimation();
            StaticAnimation success = animation.successAnimation != null ? EpicFightMod.getInstance().animationManager.findAnimationByPath(animation.successAnimation) : Animations.SWORD_GUARD_HIT;
            boolean isFront = false;
            Vec3 sourceLocation = damageSource.getSourcePosition();
            if (sourceLocation != null) {
                Vec3 viewVector = ((PathfinderMob)this.getOriginal()).getViewVector(1.0F);
                Vec3 toSourceLocation = sourceLocation.subtract(((PathfinderMob)this.getOriginal()).position()).normalize();
                if (toSourceLocation.dot(viewVector) > 0.0) {
                    isFront = true;
                }
            }

            if (this.canBlockSource(damageSource) && isFront) {
                float impact;
                if (damageSource instanceof EpicFightDamageSource) {
                    EpicFightDamageSource efDamageSource = (EpicFightDamageSource)damageSource;
                    impact = efDamageSource.getImpact();
                    if (efDamageSource.hasTag(SourceTags.GUARD_PUNCTURE)) {
                        return new AttackResult(ResultType.SUCCESS, amount);
                    }
                } else {
                    impact = amount / 3.0F;
                }

                float knockback = 0.25F + Math.min(impact * 0.1F, 1.0F);
                Entity var10 = damageSource.getDirectEntity();
                if (var10 instanceof LivingEntity) {
                    LivingEntity targetEntity = (LivingEntity)var10;
                    knockback += (float)EnchantmentHelper.getKnockbackBonus(targetEntity) * 0.1F;
                }

                float cost = this.isParry ? this.getParryCostMultiply() : this.getGuardCostMultiply();
                float stamina = this.getStamina() - impact * cost;
                this.setStamina(stamina);
                ((HitParticleType)EpicFightParticles.HIT_BLUNT.get()).spawnParticleWithArgument((ServerLevel)((PathfinderMob)this.getOriginal()).level, HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, this.getOriginal(), damageSource.getDirectEntity());
                if (!(stamina >= 0.0F)) {
                    this.setBlocking(false);
                    this.applyStun(StunType.NEUTRALIZE, 2.0F);
                    if (this.isParry) {
                        this.playSound(Sounds.BIGBONG, 0.7f,-0.31F, -0.27F);
                    } else {
                        this.playSound(Sounds.BONG, 0.7f,-0.31F, -0.27F);
                    }
                    ((HitParticleType)EpicFightParticles.AIR_BURST.get()).spawnParticleWithArgument((ServerLevel)((PathfinderMob)this.getOriginal()).level, this.getOriginal(), damageSource.getDirectEntity());
                    this.setStamina(this.getMaxStamina());
                    return new AttackResult(ResultType.SUCCESS, amount / 2.0F);
                }

                float counter_cost = this.getCounterStamina();
                Random random = ((PathfinderMob)this.getOriginal()).getRandom();
                this.rotateTo(damageSource.getDirectEntity(), 30.0F, true);
                if (random.nextFloat() < this.getCounterChance() && stamina >= counter_cost) {
                    if (this.stun_immunity_time > 0) {
                        ((PathfinderMob)this.getOriginal()).addEffect(new MobEffectInstance((MobEffect)EpicFightMobEffects.STUN_IMMUNITY.get(), this.stun_immunity_time));
                    }

                    this.setAttackSpeed(this.getCounterSpeed());
                    this.playAnimationSynchronized(this.getCounter(), 0.0F);
                    if (this.isParry) {
                        this.playSound(Sounds.BIGBONG, 0.7f,-0.31F, -0.27F);
                    } else {
                        this.playSound(Sounds.BONG, 0.7f,-0.31F, -0.27F);
                    }
                    if (this.cancel_block) {
                        this.setBlocking(false);
                    }

                    this.setStamina(this.getStamina() - counter_cost);
                } else if (this.isParry) {
                    if (this.parryCounter + 1 >= this.maxParryTimes) {
                        this.setBlocking(false);
                        if (this.stun_immunity_time > 0) {
                            ((PathfinderMob)this.getOriginal()).addEffect(new MobEffectInstance((MobEffect)EpicFightMobEffects.STUN_IMMUNITY.get(), this.stun_immunity_time));
                        }
                    }

                    this.playAnimationSynchronized(this.getParryAnimation(), 0.0F);
                    ++this.parryCounter;
                    ++this.parryTimes;
                    if (this.isParry) {
                        this.playSound(Sounds.BIGBONG, 0.7f,-0.31F, -0.27F);
                    } else {
                        this.playSound(Sounds.BONG, 0.7f,-0.31F, -0.27F);
                    }
                    this.knockBackEntity(damageSource.getDirectEntity().position(), 0.4F * knockback);
                } else {
                    this.playAnimationSynchronized(success, 0.1F);
                    if (animation.isShield) {
                        this.playSound(SoundEvents.SHIELD_BLOCK, -0.05F, 0.1F);
                    } else if (this.isParry) {
                        this.playSound(Sounds.BIGBONG, 0.7f,-0.31F, -0.27F);
                    } else {
                        this.playSound(Sounds.BONG, 0.7f,-0.31F, -0.27F);
                    }
                    this.knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                }

                Entity var14 = damageSource.getDirectEntity();
                if (var14 instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity)var14;
                    if (damageSource instanceof EntityDamageSource) {
                        AdvancedCustomHumanoidMobPatch<?> targetPatch = (AdvancedCustomHumanoidMobPatch<?>) EpicFightCapabilities.getEntityPatch(living, AdvancedCustomHumanoidMobPatch.class);
                        if (targetPatch != null) {
                            targetPatch.setParried(this.isParry);
                            targetPatch.onAttackBlocked(damageSource, this);
                        }
                    }
                }

                return new AttackResult(ResultType.BLOCKED, amount);
            }
        }

        return new AttackResult(ResultType.SUCCESS, amount);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    public void AdvancedCustomHumanoidMobPatch(Faction faction, AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider, CallbackInfo ci) {
        if (this.getOriginal() != null && this.getOriginal().hasEffect(Effect.TOUGHNESS.get())) {
            this.reganShieldMultiply = this.provider.getReganShieldMultiply()*(1f - (0.1f*this.getOriginal().getEffect(Effect.TOUGHNESS.get()).getAmplifier()));
        }
        if (this.getOriginal() != null && this.getOriginal().getEffect(Effect.TOUGHNESS.get()).getAmplifier() >= 10)
            this.reganShieldMultiply = 0.0f;

    }
}