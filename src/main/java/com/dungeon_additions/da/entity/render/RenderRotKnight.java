package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderRotKnight extends AzEntityRenderer<EntityRotKnight> {
    private static final ResourceLocation TEXTURE_ONE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/shield_1.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/shield_2.png");
    private static final ResourceLocation TEXTURE_THREE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/shield_3.png");
    private static final ResourceLocation TEXTURE_FOUR = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight/shield_4.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.rotknight.json");

    public RenderRotKnight(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityRotKnight>builder($ -> MODEL_RESLOC, entity -> entity.getSkin() == 4 ? TEXTURE_FOUR :
                entity.getSkin() == 3 ? TEXTURE_THREE : entity.getSkin() == 2 ? TEXTURE_TWO : TEXTURE_ONE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_ONE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_THREE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_FOUR))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("rotknight", false, true))
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
