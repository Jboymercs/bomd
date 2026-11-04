package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDarkRoyal extends AzEntityRenderer<EntityDarkRoyal> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/royal/dark_royal.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/royal/dark_royal_2.png");
    private static final ResourceLocation TEXTURE_THREE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/royal/dark_royal_3.png");
    private static final ResourceLocation TEXTURE_FOUR = new ResourceLocation(ModReference.MOD_ID, "textures/entity/royal/dark_royal_4.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dark_royal.json");

    public RenderDarkRoyal(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDarkRoyal>builder($ -> MODEL_RESLOC, entity ->
                                entity.getSkin() == 4 ? TEXTURE_FOUR : entity.getSkin() == 3 ? TEXTURE_THREE :
                                        entity.getSkin() == 2 ? TEXTURE_TWO :TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_THREE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FOUR))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("dark_royal", false, true,"HeadJ"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
