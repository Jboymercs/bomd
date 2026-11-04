package com.dungeon_additions.da.entity.render.generic;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDelayedExplosion extends AzEntityRenderer<EntityDelayedExplosion> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_blue.png");
    private static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_purple.png");
    private static final ResourceLocation TEXTURE_ORANGE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_orange.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/generic/geo.delayed_explosion.json");
    public RenderDelayedExplosion(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDelayedExplosion>builder($ -> MODEL_RESLOC, entity ->
                                entity.isOrangeStyle() ? TEXTURE_ORANGE : entity.isPurpleStyle() ? TEXTURE_PURPLE : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_ORANGE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_PURPLE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("delayed_explosion", true, false))
                        .setShadowRadius(0F)
                        .build()
                , renderManager);
    }
}
