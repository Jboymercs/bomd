package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;



public class RenderDraugr extends AzEntityRenderer<EntityDraugr> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_2.png");
    private static final ResourceLocation TEXTURE_THREE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_3.png");
    private static final ResourceLocation TEXTURE_FOUR = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr/draugr_4.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.draugr.json");

    public RenderDraugr(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDraugr>builder($ -> MODEL_RESLOC, entity -> entity.getSkin() == 4 ? TEXTURE_FOUR :
                                entity.getSkin() == 3 ? TEXTURE_THREE : entity.getSkin() == 2 ? TEXTURE_TWO : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_THREE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FOUR))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("draugr", false, true, "HeadJ"))

                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.25F)
                        .build()
                , renderManager);
    }

}
