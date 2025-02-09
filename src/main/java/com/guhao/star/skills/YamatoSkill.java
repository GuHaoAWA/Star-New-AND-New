package com.guhao.star.skills;

import com.google.common.collect.Maps;
import com.guhao.star.efmex.StarAnimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.*;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent.Causal;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType.HURT_EVENT_PRE;

public class YamatoSkill extends WeaponInnateSkill {
    private static final UUID EVENT_UUID = UUID.fromString("f082557a-b2f9-11eb-8529-0242ac130003");
    private final Map<ResourceLocation, Supplier<AttackAnimation>> comboAnimation = Maps.newHashMap();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    protected static final SkillDataManager.SkillDataKey<Boolean> COUNTER_SUCCESS = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
    protected static final SkillDataManager.SkillDataKey<Integer> DAMAGES = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.INTEGER);
    protected static final SkillDataManager.SkillDataKey<Boolean> POWER3 = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
    protected static final SkillDataManager.SkillDataKey<Boolean> COUNTER = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);

    public YamatoSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(COUNTER_SUCCESS);
        container.getDataManager().registerData(DAMAGES);
        container.getDataManager().registerData(POWER3);
        container.getDataManager().registerData(COUNTER);
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
            StaticAnimation animation = event.getAnimation();
            if (animation == StarAnimations.YAMATO_COUNTER2) {
                container.getDataManager().setDataSync(COUNTER_SUCCESS, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
            }

            if (animation == StarAnimations.YAMATO_STRIKE1 || animation == StarAnimations.YAMATO_STRIKE2) {
                container.getDataManager().setDataSync(COUNTER_SUCCESS, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                container.getDataManager().setDataSync(COUNTER, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
            }

            if (animation == StarAnimations.YAMATO_POWER3_FINISH || animation == StarAnimations.YAMATO_POWER_DASH) {
                container.getDataManager().setDataSync(POWER3, false, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
            }

            if (animation != StarAnimations.YAMATO_POWER3 && animation != StarAnimations.YAMATO_POWER3_REPEAT && animation != StarAnimations.YAMATO_POWER3_FINISH && animation != StarAnimations.YAMATO_POWER_DASH && animation != StarAnimations.YAMATO_POWER0_1 && container.getDataManager().getDataValue(DAMAGES) > 1) {
                container.getDataManager().setData(DAMAGES, 0);
            }

            if (animation != StarAnimations.YAMATO_POWER3 && animation != StarAnimations.YAMATO_POWER3_REPEAT && container.getDataManager().getDataValue(POWER3)) {
                container.getDataManager().setData(POWER3, false);
            }

            if (animation != StarAnimations.YAMATO_AUTO1 && animation != StarAnimations.YAMATO_AUTO2 && animation != StarAnimations.YAMATO_AUTO3 && animation != StarAnimations.YAMATO_AUTO4 && animation != StarAnimations.YAMATO_POWER1 && animation != StarAnimations.YAMATO_POWER2 && animation != StarAnimations.YAMATO_POWER3 && animation != StarAnimations.YAMATO_POWER3_REPEAT && animation != StarAnimations.YAMATO_POWER3_FINISH && animation != StarAnimations.YAMATO_STRIKE1 && animation != StarAnimations.YAMATO_STRIKE2 && animation != StarAnimations.YAMATO_COUNTER1) {
                BasicAttack.setComboCounterWithEvent(Causal.BASIC_ATTACK_COUNT, event.getPlayerPatch(), event.getPlayerPatch().getSkill(SkillSlots.BASIC_ATTACK), null, 0);
            }

        });
        listener.addEventListener(EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, (event) -> {
            int id = event.getAnimation().getId();
            if (id != StarAnimations.YAMATO_POWER3.getId() && id != StarAnimations.YAMATO_POWER3_REPEAT.getId()) {
                if (id == StarAnimations.YAMATO_COUNTER1.getId()) {
                    container.getDataManager().setDataSync(COUNTER_SUCCESS, true, ((ServerPlayerPatch)container.getExecuter()).getOriginal());
                    event.getPlayerPatch().reserveAnimation(StarAnimations.YAMATO_COUNTER2);
                } else if (id == StarAnimations.YAMATO_POWER3_FINISH.getId() || id == StarAnimations.YAMATO_POWER_DASH.getId()) {
                    container.getDataManager().setData(DAMAGES, 0);
                }
            } else {
                event.getPlayerPatch().reserveAnimation(StarAnimations.YAMATO_POWER3_FINISH);
            }

        });
        listener.addEventListener(EventType.BASIC_ATTACK_EVENT, EVENT_UUID, (event) -> {
            float stamina = event.getPlayerPatch().getStamina();
            ResourceLocation rl = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
            if ((rl == StarAnimations.YAMATO_POWER3.getRegistryName() || rl == StarAnimations.YAMATO_POWER3_REPEAT.getRegistryName()) && stamina < 2.0F) {
                event.setCanceled(true);
            }

        });
        listener.addEventListener(EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, (event) -> {
            Integer k = container.getDataManager().getDataValue(DAMAGES);
            int id = event.getDamageSource().getAnimation().getId();
            float maxstamina = event.getPlayerPatch().getMaxStamina();
            float stamina = event.getPlayerPatch().getStamina();
            float recover = maxstamina * 0.01F;
            if (id == StarAnimations.YAMATO_POWER3.getId() || id == StarAnimations.YAMATO_POWER3_REPEAT.getId()) {
                event.getPlayerPatch().setStamina(stamina + recover);
                container.getDataManager().setData(DAMAGES, k + 1);
            }

            float c;
            if (id == StarAnimations.YAMATO_POWER1.getId()) {
                c = 0.25F;
                if (stamina < maxstamina) {
                    container.getExecuter().setStamina(stamina + c * maxstamina);
                }
            } else if (id == StarAnimations.YAMATO_COUNTER2.getId()) {
                c = 0.1F;
                if (stamina < maxstamina) {
                    container.getExecuter().setStamina(stamina + c * maxstamina);
                }
            }

        });
        listener.addEventListener(EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, (event) -> {
            ResourceLocation rl = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
            Integer K = container.getDataManager().getDataValue(DAMAGES);
            int max = 25;
            if (rl == StarAnimations.YAMATO_POWER3_REPEAT.getRegistryName() || rl == StarAnimations.YAMATO_POWER3.getRegistryName()) {
                container.getDataManager().setData(DAMAGES, K + 1);
            }

            float bonus = 0.15F;
            if (rl == StarAnimations.YAMATO_POWER3_FINISH.getRegistryName() || rl == StarAnimations.YAMATO_POWER_DASH.getRegistryName()) {
                K = Math.min(K, max);
                event.setDamage(event.getDamage() * (1.0F + bonus * (float)K));
            }

        });
        listener.addEventListener(HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            int power2_recover = 2;
            Skill skill = container.getExecuter().getSkill(SkillSlots.WEAPON_INNATE).getSkill();
            ServerPlayerPatch executer = event.getPlayerPatch();
            AnimationPlayer animationPlayer = executer.getAnimator().getPlayerFor(null);
            float elapsedTime = animationPlayer.getElapsedTime();
            int animationId = executer.getAnimator().getPlayerFor(null).getAnimation().getId();
            if (elapsedTime <= 0.35F) {
                if (animationId == StarAnimations.YAMATO_POWER0_1.getId()) {
                    DamageSource damagesource = event.getDamageSource();
                    Vec3 sourceLocation = damagesource.getSourcePosition();
                    if (sourceLocation != null) {
                        Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
                        Vec3 toSourceLocation = sourceLocation.subtract(event.getPlayerPatch().getOriginal().position()).normalize();
                        if (toSourceLocation.dot(viewVector) > 0.0D) {
                            if (damagesource instanceof EntityDamageSource
                                    && !damagesource.isExplosion()
                                    && !damagesource.isMagic()
                                    && !damagesource.isBypassArmor()
                                    && !damagesource.isBypassInvul()) {
                                POWER0_2(executer);
                                container.getDataManager().setDataSync(COUNTER_SUCCESS, true, ((ServerPlayerPatch) container.getExecuter()).getOriginal());
                                scheduler.schedule(() -> executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().setData(COUNTER_SUCCESS, false), 1500, TimeUnit.MILLISECONDS);
                                if (skill != null) {
                                    this.stackCost(executer, -power2_recover);
                                } else {
                                    event.getPlayerPatch().playAnimationSynchronized(StarAnimations.YAMATO_POWER0_2, 0.15F);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID);
    }

    private void STRIKE2(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_STRIKE2, 0F);
    }

    private void POWER_1(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER1, 0.0F);
    }

    private void POWER_2(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER2, 0.0F);
    }

    private void POWER_3(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER3, 0.15F);
    }

    private void POWER_DASH(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER_DASH, 0.25F);
    }

    private void POWER0_1(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER0_1, 0.0F);
    }

    private void POWER0_2(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER0_2, 0.05F);
    }

    private void COUNTER(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_COUNTER1, 0.25F);
    }
    private void POWER_DASHS(ServerPlayerPatch executer) {
        executer.playAnimationSynchronized(StarAnimations.YAMATO_POWER_DASH, -0.3F);
    }

    public boolean canExecute(PlayerPatch<?> executer) {
        ResourceLocation rl = executer.getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
        if (rl == StarAnimations.YAMATO_POWER3.getRegistryName() || rl == StarAnimations.YAMATO_POWER3_REPEAT.getRegistryName()
                || rl == StarAnimations.YAMATO_ACTIVE_GUARD_HIT.getRegistryName() || rl == StarAnimations.YAMATO_ACTIVE_GUARD_HIT2.getRegistryName()
                || rl == StarAnimations.YAMATO_COUNTER1.getRegistryName() || rl == StarAnimations.YAMATO_POWER0_2.getRegistryName()) {
            return true;
        } else if (executer.isLogicalClient()) {
            return executer.getEntityState().canBasicAttack();
        } else {
            ItemStack itemstack = executer.getOriginal().getMainHandItem();
            return executer.getHoldingItemCapability(InteractionHand.MAIN_HAND).getInnateSkill(executer, itemstack) == this;
        }
    }
    public WeaponInnateSkill registerPropertiesToAnimation() {
        return null;
    }

    private void stackCost(ServerPlayerPatch player, int cost) {
        this.setStackSynchronize(player, player.getSkill(StarSkill.YAMATOSKILL).getStack() - cost);
    }

    @Override
    public void executeOnServer(ServerPlayerPatch execute, FriendlyByteBuf args) {
        ResourceLocation rl = execute.getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
        SkillContainer skillContainer = execute.getSkill(SkillSlots.WEAPON_INNATE);
        Boolean counterSuccess = skillContainer.getDataManager().getDataValue(COUNTER_SUCCESS);
        Boolean counter = skillContainer.getDataManager().getDataValue(COUNTER);
        Boolean power3 = skillContainer.getDataManager().getDataValue(POWER3);
        if (power3) {
            POWER_DASHS(execute);
        }
        if (counterSuccess) {
            STRIKE2(execute);
            return;
        }
        if (counter) {
            STRIKE2(execute);
            return;
        }
        if (execute.getOriginal().isSprinting() && execute.getSkill(StarSkill.YAMATOSKILL).getStack() >= 0) {
            float stamina = execute.getStamina();
            float maxStamina = execute.getMaxStamina();
            float p = maxStamina * 0.25F;
            if (stamina >= p) {
                POWER_DASH(execute);
                execute.setStamina(stamina - p);
            }
        } else {
            if (this.comboAnimation.containsKey(rl)) {
                execute.playAnimationSynchronized(this.comboAnimation.get(rl).get(), 0.0F);
            } else {
                if (canExecute(execute) && !power3) {
                    POWER0_1(execute);
                } else {
                    return;
                }
                Map<ResourceLocation, Runnable> actionMap = Maps.newHashMap();
                actionMap.put(StarAnimations.YAMATO_AUTO1.getRegistryName(), () -> POWER_1(execute));
                actionMap.put(StarAnimations.YAMATO_AUTO2.getRegistryName(), () -> POWER_2(execute));
                actionMap.put(StarAnimations.YAMATO_AUTO3.getRegistryName(), () -> POWER_3(execute));
                actionMap.put(StarAnimations.YAMATO_POWER3.getRegistryName(), () -> POWER_DASHS(execute));
                actionMap.put(StarAnimations.YAMATO_POWER3_REPEAT.getRegistryName(), () -> POWER_DASHS(execute));
                actionMap.put(StarAnimations.YAMATO_STRIKE1.getRegistryName(), () -> POWER_2(execute));
                actionMap.put(StarAnimations.YAMATO_STRIKE2.getRegistryName(), () -> POWER_3(execute));
                actionMap.put(StarAnimations.YAMATO_COUNTER1.getRegistryName(), () -> STRIKE2(execute));
                actionMap.put(StarAnimations.YAMATO_ACTIVE_GUARD_HIT.getRegistryName(), () -> COUNTER(execute));
                actionMap.put(StarAnimations.YAMATO_ACTIVE_GUARD_HIT2.getRegistryName(), () -> COUNTER(execute));
                if (actionMap.containsKey(rl)) {
                    actionMap.get(rl).run();
                }
                super.executeOnServer(execute, args);
            }
        }
    }
}
