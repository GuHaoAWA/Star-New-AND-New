
package com.guhao.star.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class PersonaItem extends Item implements ICurioItem {
	public PersonaItem() {
		super(new Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public boolean isFoil(@NotNull ItemStack itemstack) {
//		return true;
//	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemstack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TextComponent("§l§o§5Persona!"));
		list.add(new TextComponent("使所有魔法或无视护甲的攻击均*1.5倍释放，获得无限夜视效果"));
		list.add(new TextComponent("战斗模式下有特殊UI界面"));
	}
}
