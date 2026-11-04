package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.animation.AzLib.animators.RotKnightBossAnimator;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderRotKnightBoss extends AzEntityRenderer<EntityRotKnightBoss> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/fallen.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/fallen_eyes.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.fallen.json");

    public RenderRotKnightBoss(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityRotKnightBoss>builder($ -> MODEL_RESLOC, entity ->
                                entity.isHideArms() ? TEXTURE : TEXTURE_TWO)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new RotKnightBossAnimator("fallen"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
