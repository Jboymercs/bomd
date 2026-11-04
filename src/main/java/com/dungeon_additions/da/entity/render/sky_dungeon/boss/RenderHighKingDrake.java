package com.dungeon_additions.da.entity.render.sky_dungeon.boss;

import com.dungeon_additions.da.animation.AzLib.animators.HighDrakeAnimator;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHighKingDrake extends AzEntityRenderer<EntityHighKingDrake> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king_drake.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/high_king/geo.high_king_drake.json");

    public RenderHighKingDrake(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityHighKingDrake>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new HighDrakeAnimator("high_king_drake"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.9F)
                        .build()
                , renderManager);
    }

}
