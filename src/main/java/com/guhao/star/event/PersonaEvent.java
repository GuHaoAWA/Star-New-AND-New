package com.guhao.star.event;

import com.guhao.star.regirster.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;


@Mod.EventBusSubscriber
public class PersonaEvent {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent event) {
		if ((event.getSource().isBypassArmor() || event.getSource().isMagic()) && event.getSource().getEntity() instanceof LivingEntity livingEntity && CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, Items.PERSONA.get()).isPresent()) {
			event.setAmount(event.getAmount() * 1.5f);
		}
	}
}
