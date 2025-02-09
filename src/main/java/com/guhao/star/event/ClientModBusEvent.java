package com.guhao.star.event;

import com.guhao.star.Config;
import com.guhao.star.regirster.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.guhao.star.Star.MODID;


@Mod.EventBusSubscriber
public class ClientModBusEvent {
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void RenderRegistry(final PatchedRenderersEvent.Add event) {
//        //sheath
//        List<? extends String> katanaitem = Config.KATANA_ITEM.get();
//        for (String katana : katanaitem) {
//            String[] entry = katana.split(" ");
//            Item katanaItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry[0]));
////            event.addItemRenderer(katanaItem, new RenderCustomKatana());
//        }
//    }

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        Config.load();
        event.enqueueWork(() -> ItemProperties.register(Items.CUSTOM_SHEATH.get(),
                new ResourceLocation(MODID, "custom"),
                (Stack, World, Entity, i) -> Stack.getOrCreateTag().getFloat("custom_sheath")));
        event.enqueueWork(() -> ItemProperties.register(Items.DEFENSE.get(),
                new ResourceLocation(MODID, "defense"),
                (Stack, World, Entity, i) -> Stack.getOrCreateTag().getFloat("enable_defense")));
    }

}
