package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFriendlyCursedRevenant  extends AzEntityRenderer<EntityFriendlyCursedRevenant> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/reanimate_friendly.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.reanimate.json");

    public RenderFriendlyCursedRevenant(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityFriendlyCursedRevenant>builder(MODEL_RESLOC, TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("reanimate", false, true))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.25F)
                        .build()
                , renderManager);
    }

}
