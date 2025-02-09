package com.guhao.star.mixins;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mixin(
        value = {PlayerPatch.class},
        remap = false
)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {

    @Inject(method = "getDamageSource",at = @At("TAIL"))
    public void getDamageSource(StaticAnimation animation, InteractionHand hand, CallbackInfoReturnable<EpicFightDamageSource> cir) {
        Player p = this.getOriginal();
        EpicFightDamageSource damagesource = EpicFightDamageSource.commonEntityDamageSource("player", this.original, animation);
        if (p.hasEffect(Effect.REALLY_STUN_IMMUNITY.get())) {
            damagesource.setImpact(0);
        } else {
            damagesource.setImpact(this.getImpact(hand));
        }
    }
    @Shadow
    protected int tickSinceLastAction;
    @Shadow
    protected double xo;
    @Shadow
    protected double yo;
    @Shadow
    protected double zo;

    public PlayerPatchMixin() {
    }

    @Shadow
    public float getMaxStamina() {
        AttributeInstance maxStamina = ((Player)this.original).getAttribute((Attribute)EpicFightAttributes.MAX_STAMINA.get());
        return (float)(maxStamina == null ? 0.0 : maxStamina.getValue());
    }

    @Shadow
    public float getStamina() {
        return this.getMaxStamina() == 0.0F ? 0.0F : (Float)((Player)this.original).getEntityData().get(PlayerPatch.STAMINA);
    }

    @Shadow
    public void setStamina(float value) {
        float f1 = Math.max(Math.min(value, this.getMaxStamina()), 0.0F);
        ((Player)this.original).getEntityData().set(PlayerPatch.STAMINA, f1);
    }

    @Override
    public void updateMotion(boolean b) {
    }

    protected void initAttributes() {
        super.initAttributes();
        this.original.getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(12.0);
        this.original.getAttribute(EpicFightAttributes.STAMINA_REGEN.get()).setBaseValue(1.0);
        this.original.getAttribute(EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        if (this.original.getVehicle() != null) {
            return Animations.BIPED_HIT_ON_MOUNT;
        } else {
            switch(stunType) {
                case LONG:
                    return Animations.BIPED_HIT_LONG;
                case SHORT, HOLD:
                    return Animations.BIPED_HIT_SHORT;
                case KNOCKDOWN:
                    return Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE:
                    return Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL:
                    return Animations.BIPED_LANDING;
                case NONE:
                    return null;
            }
        }

        return null;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void serverTick(LivingEvent.LivingUpdateEvent event) {
        super.serverTick(event);
        if (this.state.canBasicAttack()) {
            ++tickSinceLastAction;
        }

        float stamina = getStamina();
        float maxStamina = getMaxStamina();
        float staminaRegen = (float) this.original.getAttributeValue(EpicFightAttributes.STAMINA_REGEN.get());
        int regenStandbyTime = 900 / (int)(150.0F * staminaRegen);
        if (stamina < maxStamina && tickSinceLastAction > regenStandbyTime) {
            float staminaFactor = 1.0F;
            setStamina(stamina + staminaFactor * (0.075F + maxStamina * 0.00625F) * staminaRegen);
        }

        if (maxStamina < stamina) {
            setStamina(maxStamina);
        }

        xo = this.original.getX();
        yo = this.original.getY();
        zo = this.original.getZ();
    }

}