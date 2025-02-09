package com.guhao.star.event;

import com.guhao.star.regirster.Effect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.guhao.star.Star.MODID;

@Mod.EventBusSubscriber(
        modid = MODID
)
public class ArmorDurabilityHandlerEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();

        // 检查实体是否为玩家
        if (entity instanceof Player player) {

            // 检查玩家是否拥有特定的buff
            if (!player.hasEffect(Effect.EXECUTE.get()) || !player.hasEffect(Effect.EXECUTED.get())) {
                // 遍历玩家的所有盔甲槽
                for (ItemStack armorStack : player.getArmorSlots()) {
                    // 检查盔甲是否有耐久度
                    if (armorStack.isDamageableItem()) {
                        // 计算盔甲应损耗的耐久度
                        int damage = (int) Math.ceil(event.getAmount() * 4.0F);
                        // 减少盔甲的耐久度
                        armorStack.hurtAndBreak(damage, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                    }
                }
            }
        }
    }
}
