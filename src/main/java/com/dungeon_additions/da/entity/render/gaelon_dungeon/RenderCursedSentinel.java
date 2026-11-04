package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.CursedSentinelAnimator;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityCursedSentinel;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCursedSentinel extends AzEntityRenderer<EntityCursedSentinel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem.png");
    private static final ResourceLocation TEXTURE_INANIMATE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_inanimate.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.cursed_golem.json");

    public RenderCursedSentinel(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityCursedSentinel>builder($ -> MODEL_RESLOC, entity ->
                                entity.isInAnimateState() ? TEXTURE_INANIMATE :TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_INANIMATE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new CursedSentinelAnimator("cursed_golem"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.6F)
                        .build()
                , renderManager);
    }
}
