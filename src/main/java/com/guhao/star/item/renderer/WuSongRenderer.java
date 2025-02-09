package com.guhao.star.item.renderer;

import com.guhao.star.item.WuSongItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class WuSongRenderer extends GeoItemRenderer<WuSongItem> {

    public WuSongRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet, AnimatedGeoModel<WuSongItem> modelProvider) {
        super(dispatcher, modelSet, modelProvider);
    }

    public WuSongRenderer(AnimatedGeoModel<WuSongItem> modelProvider) {
        super(modelProvider);
    }

    public WuSongRenderer() {
        super(new AnimatedGeoModel<>() {

            @Override
            public ResourceLocation getAnimationFileLocation(WuSongItem wuSongItem) {
                return new ResourceLocation("star", "animations/wusong.animation.json");
            }

            @Override
            public ResourceLocation getModelLocation(WuSongItem wuSongItem) {
                return new ResourceLocation("star", "geo/wusong.geo.json");
            }

            @Override
            public ResourceLocation getTextureLocation(WuSongItem wuSongItem) {
                return new ResourceLocation("star", "textures/items/wusong.png");
            }
        });
    }


}
