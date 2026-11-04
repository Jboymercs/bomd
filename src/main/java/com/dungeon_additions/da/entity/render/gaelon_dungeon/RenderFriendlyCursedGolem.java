package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.FCursedSentinelAnimator;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityFriendlyCursedGolem;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFriendlyCursedGolem extends AzEntityRenderer<EntityFriendlyCursedGolem> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_friendly.png");
    private static final ResourceLocation TEXTURE_INANIMATE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_inanimate.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.cursed_golem.json");

    public RenderFriendlyCursedGolem(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityFriendlyCursedGolem>builder($ -> MODEL_RESLOC, entity ->
                                entity.isInAnimateState() ? TEXTURE_INANIMATE :TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_INANIMATE))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new FCursedSentinelAnimator("cursed_golem"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.6F)
                        .build()
                , renderManager);
    }
}
