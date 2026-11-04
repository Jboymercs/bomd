package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.animators.VoidBlossomAnimator;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderVoidBlossom  extends AzEntityRenderer<EntityVoidBlossom> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/blossom.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/blossom/blossom.geo.json");

    public RenderVoidBlossom(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityVoidBlossom>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new VoidBlossomAnimator("blossom"))

                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.9F)
                        .build()
                , renderManager);
    }
}
