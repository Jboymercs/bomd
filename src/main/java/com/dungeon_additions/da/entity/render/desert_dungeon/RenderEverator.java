package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.EveratorAnimator;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.EntityEverator;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEverator extends AzEntityRenderer<EntityEverator> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/everator.png");
    private static final ResourceLocation TEXTURE_INACTIVE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/everator_inactive.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.everator.json");


    public RenderEverator(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityEverator>builder($ -> MODEL_RESLOC, entity ->
                                entity.isInactiveMode() || entity.isAwake() ? TEXTURE_INACTIVE : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_INACTIVE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new EveratorAnimator("everator"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }

}
