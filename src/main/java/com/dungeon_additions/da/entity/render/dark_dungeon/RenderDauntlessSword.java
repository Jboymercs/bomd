package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessSword;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDauntlessSword extends AzEntityRenderer<EntityDauntlessSword> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_sword.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dauntless_sword.json");

    public RenderDauntlessSword(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityDauntlessSword>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("dauntless_sword", true, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.3F)
                        .build()
                , renderManager);
    }
}
