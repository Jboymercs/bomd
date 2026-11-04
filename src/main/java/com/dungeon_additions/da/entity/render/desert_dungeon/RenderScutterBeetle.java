package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderScutterBeetle extends AzEntityRenderer<EntityScutterBeetle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/scutter_beetle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.scutter_beetle.json");
    public RenderScutterBeetle(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityScutterBeetle>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("scutter_beetle", false, true, "HeadJoint"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
