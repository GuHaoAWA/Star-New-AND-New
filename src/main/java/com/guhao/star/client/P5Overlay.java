package com.guhao.star.client;

import com.guhao.star.regirster.Items;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class P5Overlay {

    final static ResourceLocation[] textures = generateTextures();
    private static int currentTextureIndex = 0;

    private static ResourceLocation[] generateTextures() {
        ResourceLocation[] textures = new ResourceLocation[482 + 1];
        for (int i = 0; i <= 482; i++) {
            String formattedNumber = String.format("%03d", i);
            textures[i] = new ResourceLocation(com.guhao.star.Star.MODID, "textures/screens/p" + formattedNumber + ".png");
        }
        return textures;
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHandler(RenderGameOverlayEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayerPatch lpp = EpicFightCapabilities.getEntityPatch(minecraft.player, LocalPlayerPatch.class);
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            int w = event.getWindow().getGuiScaledWidth();
            int h = event.getWindow().getGuiScaledHeight();
            int posX = w / 2;
            int posY = h / 2;
            Level world = null;
            double x = 0;
            double y = 0;
            double z = 0;
            Player entity = Minecraft.getInstance().player;
            if (entity != null) {
                world = entity.level;
                x = entity.getX();
                y = entity.getY();
                z = entity.getZ();
            }
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            if (lpp != null && lpp.isBattleMode() && CuriosApi.getCuriosHelper().findFirstCurio(minecraft.player, Items.PERSONA.get()).isPresent()) {
                // 使用当前纹理
                RenderSystem.setShaderTexture(0, textures[currentTextureIndex]);
                GuiComponent.blit(event.getMatrixStack(), 0, 0, 0, 0, w, h, w, h);

                // 更新当前纹理索引，循环播放
                currentTextureIndex = (currentTextureIndex + 1) % textures.length;
            }
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }
}
