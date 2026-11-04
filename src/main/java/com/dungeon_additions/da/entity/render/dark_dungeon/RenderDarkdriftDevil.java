package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.DarkdriftDevilAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.render.dark_dungeon.layer.RenderLayerDemonBuff;
import com.dungeon_additions.da.entity.render.dark_dungeon.layer.RenderLayerDemonShield;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderDarkdriftDevil  extends AzEntityRenderer<EntityDarkdriftDevil> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/darkdrift_devil.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.darkdrift_devil.json");

    public RenderDarkdriftDevil(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDarkdriftDevil>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DarkdriftDevilAnimator("darkdrift_devil"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new RenderLayerDemonBuff())
                        .addRenderLayer(new RenderLayerDemonShield())
                        .setShadowRadius(0.5F)
                        .build()
                , renderManager);
    }
}
