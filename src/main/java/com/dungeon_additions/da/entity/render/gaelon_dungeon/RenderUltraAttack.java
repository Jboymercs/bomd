package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityUltraAttack;
import com.dungeon_additions.da.entity.render.layer.LayerUltraAttack;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderUltraAttack extends AzEntityRenderer<EntityUltraAttack> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/ultra_attack.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.ultra_attack.json");

    public RenderUltraAttack(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityUltraAttack>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("ultra_attack", true, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                         .addRenderLayer(new LayerUltraAttack())
                        .setShadowRadius(0F)
                        .build()
                , renderManager);
    }

}
