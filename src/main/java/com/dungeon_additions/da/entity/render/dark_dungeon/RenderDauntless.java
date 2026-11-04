package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.DauntlessAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDauntless extends AzEntityRenderer<EntityDauntless> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_3.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dauntless.json");

    public RenderDauntless(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDauntless>builder($ -> MODEL_RESLOC, entity ->
                                entity.getSwordCharge() == 1 ? TEXTURE_1 : entity.getSwordCharge() == 2 ? TEXTURE_2 :
                                        entity.getSwordCharge() >= 3 ? TEXTURE_3 : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_1))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_2))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_3))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DauntlessAnimator("dauntless"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.4F)
                        .build()
                , renderManager);
    }

}
