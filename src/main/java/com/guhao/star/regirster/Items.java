package com.guhao.star.regirster;

import com.guhao.star.item.PersonaItem;
import com.guhao.star.item.WuSongItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.star.Star.MODID;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> CUSTOM_SHEATH = ITEMS.register("custom_sheath", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> DEFENSE = ITEMS.register("defense", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> HUA = ITEMS.register("hua", () -> new RecordItem(666, () -> Sounds.YILIANJIEYVAN, (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> PERSONA = ITEMS.register("persona", PersonaItem::new);
    public static final RegistryObject<Item> WUSONG = ITEMS.register("wusong", WuSongItem::new);
}

