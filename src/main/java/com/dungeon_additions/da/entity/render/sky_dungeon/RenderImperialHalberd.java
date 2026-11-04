package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderImperialHalberd extends AzEntityRenderer<EntityImperialHalberd> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/imperial_halberd.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.imperial_halberd.json");

    public RenderImperialHalberd(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityImperialHalberd>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("imperial_halberd", false, true, "HeadJ"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.35F)
                        .build()
                , renderManager);
    }

}
