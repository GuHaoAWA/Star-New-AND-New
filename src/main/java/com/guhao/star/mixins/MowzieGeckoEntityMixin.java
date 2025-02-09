package com.guhao.star.mixins;

import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityCapability;
import com.bobmowzie.mowziesmobs.server.capability.CapabilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenCapability;
import com.bobmowzie.mowziesmobs.server.entity.IAnimationTickable;
import com.bobmowzie.mowziesmobs.server.entity.MMBossInfoServer;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

@Mixin(value = MowzieGeckoEntity.class,remap = false)
public abstract class MowzieGeckoEntityMixin extends MowzieEntity implements IAnimatable, IAnimationTickable {
    public MowzieGeckoEntityMixin(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }
    @Shadow
    public AbilityCapability.IAbilityCapability getAbilityCapability() {
        return AbilityHandler.INSTANCE.getAbilityCapability(this);
    }
    @Shadow
    public MowzieAnimationController<MowzieGeckoEntity> getController() {
        return null;
    }
    @Shadow
    protected <E extends IAnimatable> void loopingAnimations(AnimationEvent<E> event) {
        event.getController().setAnimation((new AnimationBuilder()).addAnimation("idle"));
    }

    private final MMBossInfoServer bossInfo = new MMBossInfoServer(this);

    @Shadow protected MowzieAnimationController<MowzieGeckoEntity> controller;

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AbilityCapability.IAbilityCapability abilityCapability = this.getAbilityCapability();
        FrozenCapability.IFrozenCapability frozenCapability = CapabilityHandler.getCapability(this, CapabilityHandler.FROZEN_CAPABILITY);
        if (abilityCapability == null) {
            return PlayState.STOP;
        } else if (this.hasEffect(Effect.DING.get())) {
            event.getController().animationSpeed = 0;
            this.controller.animationSpeed = 0;
            return PlayState.STOP;
        } else if (frozenCapability != null && frozenCapability.getFrozen()) {
            return PlayState.STOP;
        } else if (abilityCapability.getActiveAbility() != null) {
            this.getController().transitionLengthTicks = 0.0;
            return abilityCapability.animationPredicate(event, null);
        } else {
            this.loopingAnimations(event);
            return PlayState.CONTINUE;
        }
    }
    @Unique
    @Override
    public void tick() {
        if (this.hasEffect(Effect.DING.get())) {
            this.controller.animationSpeed = 0;
        }
        this.prevPrevOnGround = this.prevOnGround;
        this.prevOnGround = this.onGround;
        super.tick();
        ++this.frame;
        if (this.tickCount % 4 == 0) {
            this.bossInfo.update();
        }

        if (this.getTarget() != null) {
            this.targetDistance = this.distanceTo(this.getTarget()) - this.getTarget().getBbWidth() / 2.0F;
            this.targetAngle = (float)this.getAngleBetweenEntities(this, this.getTarget());
        }

        this.willLandSoon = !this.onGround && this.level.noCollision(this.getBoundingBox().move(this.getDeltaMovement()));
        if (!this.level.isClientSide && this.getBossMusic() != null) {
            if (this.canPlayMusic()) {
                this.level.broadcastEntityEvent(this, (byte)67);
            } else {
                this.level.broadcastEntityEvent(this, (byte)68);
            }
        }

    }
}
