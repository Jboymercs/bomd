package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBlueWave extends AzEntityRenderer<EntityBlueWave> {

    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/blue.png");
    private static final ResourceLocation TEXTURE_RED = new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red.png");
    private static final ResourceLocation TEXTURE_ORANGE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/orange.png");
    private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/wave/wave.geo.json");

    public RenderBlueWave(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityBlueWave>builder($ -> MODEL_RESLOC, entity ->
                                entity.isPurpleStyle() ? TEXTURE_PURPLE : entity.isFlameStyle() ? TEXTURE_ORANGE :
                                        entity.isRedStyle() ? TEXTURE_RED : TEXTURE_BLUE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_BLUE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_ORANGE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_PURPLE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_RED))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("wave", true, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0F)
                        .build()
                , renderManager);
    }
}
