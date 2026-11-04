package com.dungeon_additions.da.entity.render.sky_dungeon.boss;

import com.dungeon_additions.da.animation.AzLib.animators.HighKingAnimator;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHighKing extends AzEntityRenderer<EntityHighKing> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king.png");
    private static final ResourceLocation TEXTURE_BLODDY = new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king_bloody.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/high_king/geo.high_king.json");

    public RenderHighKing(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityHighKing>builder($ -> MODEL_RESLOC, entity ->
                                entity.isBloodied() ? TEXTURE_BLODDY : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_BLODDY))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new HighKingAnimator("high_king"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.35F)
                        .build()
                , renderManager);
    }
}
