package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.void_dungeon.EntityEnderphriteGauntlet;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEnderphriteGauntlet extends AzEntityRenderer<EntityEnderphriteGauntlet> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/enderphrite_gauntlet.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.enderphrite.json");

    public RenderEnderphriteGauntlet(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityEnderphriteGauntlet>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("enderphrite", false, true))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }

}
