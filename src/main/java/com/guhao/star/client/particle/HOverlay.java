package com.guhao.star.client.particle;

import com.guhao.star.Config;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class HOverlay {

    private static final String[] DANMAKU_TEXTS = {"§f終焉決斗場由§4凝星製作組§f傾情製作，由澳門京城等贊助，本整合包資源僅供§6學習交流§f，請勿用於其§1他用途徑"};
    private static final int DANMAKU_SPEED = 1; // 每帧移动的像素数
    private static int[] danmakuXPositions; // 存储每个弹幕的X坐标
    private static final int DANMAKU_Y_POSITION = 10;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGameOverlayEvent.Pre event) {
        boolean text = Config.TEXT.get();
        if (!text) return;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayerPatch lpp = EpicFightCapabilities.getEntityPatch(minecraft.player, LocalPlayerPatch.class);
        if (danmakuXPositions == null) {
            danmakuXPositions = new int[DANMAKU_TEXTS.length];
            for (int i = 0; i < DANMAKU_TEXTS.length; i++) {
                danmakuXPositions[i] = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            int w = event.getWindow().getGuiScaledWidth();
            int h = event.getWindow().getGuiScaledHeight();

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
            for (int i = 0; i < DANMAKU_TEXTS.length; i++) {
                danmakuXPositions[i] -= DANMAKU_SPEED;
                if (danmakuXPositions[i] < -Minecraft.getInstance().font.width(DANMAKU_TEXTS[i])) {
                    danmakuXPositions[i] = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                }
            }
            for (int i = 0; i < DANMAKU_TEXTS.length; i++) {
                Minecraft.getInstance().font.draw(event.getMatrixStack(), DANMAKU_TEXTS[i], danmakuXPositions[i], DANMAKU_Y_POSITION, 0xFFFFFF);
            }

            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }
}
