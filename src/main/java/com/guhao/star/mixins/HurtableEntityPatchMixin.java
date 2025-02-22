package com.guhao.star.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;

@Mixin(value = HurtableEntityPatch.class,remap = false)
public abstract class HurtableEntityPatchMixin <T extends LivingEntity> extends EntityPatch<T> {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void knockBackEntity(Vec3 sourceLocation, float power) {
        double d1 = sourceLocation.x() - ((LivingEntity)this.original).getX();

        double d0;
        for(d0 = sourceLocation.z() - ((LivingEntity)this.original).getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
            d1 = (Math.random() - Math.random()) * 0.01;
        }

        power = (float)((double)power * (1.0 - ((LivingEntity)this.original).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        if ((double)power > 0.0) {
            ((LivingEntity)this.original).hasImpulse = true;
            ((LivingEntity)this.original).hurtMarked = true;//对，不知道是他反混淆名有问题还是怎么着，加上这行才行
            Vec3 vec3 = ((LivingEntity)this.original).getDeltaMovement();

            Vec3 vec31 = (new Vec3(d1, 0.0, d0)).normalize().scale((double)power);
            ((LivingEntity)this.original).setDeltaMovement(vec3.x / 2.0 - vec31.x, ((LivingEntity)this.original).onGround ? Math.min(0.4, vec3.y / 2.0) : vec3.y, vec3.z / 2.0 - vec31.z);
        }

    }

}