package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPyre extends AzEntityRenderer<EntityPyre> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre.png");
    private static final ResourceLocation TEXTURE_LIT = new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre_lit.png");
    private static final ResourceLocation TEXTURE_CRACKED = new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre_cracked.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/pyre/geo.pyre.json");
    public RenderPyre(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityPyre>builder($ -> MODEL_RESLOC,entity ->
                                entity.isFightMode() && !entity.isCracked ? TEXTURE_LIT :
                                        entity.isCracked ? TEXTURE_CRACKED : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                .setRenderType(RenderType.entityTranslucent(TEXTURE_LIT))
                .setRenderType(RenderType.entityTranslucent(TEXTURE_CRACKED))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("pyre", true, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.65F)
                        .build()
                , renderManager);
    }
}
