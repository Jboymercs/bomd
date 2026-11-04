package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianColossus;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAegyptianColossus extends AzEntityRenderer<EntityAegyptianColossus> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_colossus.png");
    private static final ResourceLocation TEXTURE_TRANSITION = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_colossus_transition.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.aegyptian_colossus.json");

    public RenderAegyptianColossus(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityAegyptianColossus>builder($ -> MODEL_RESLOC, entity ->
                                entity.isPhaseTransition() ? TEXTURE_TRANSITION : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TRANSITION))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("aegyptian_colossus", false, true))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new LayerColossusShield())
                        .setShadowRadius(0.8F)
                        .build()
                , renderManager);
    }

}
