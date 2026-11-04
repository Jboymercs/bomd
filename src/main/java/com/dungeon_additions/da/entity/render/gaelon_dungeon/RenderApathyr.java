package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.animation.AzLib.animators.ApathyrAnimator;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.util.ModReference;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;


public class RenderApathyr extends AzEntityRenderer<EntityApathyr> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr.png");
    private static final ResourceLocation TEXTURE_TRANSITION = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_broken.png");
    private static final ResourceLocation TEXTURE_IDLE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_idle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.apathyr.json");

    public RenderApathyr(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityApathyr>builder($ -> MODEL_RESLOC, entity ->
                                entity.isIdleState() ? TEXTURE_IDLE : entity.getHealth() / entity.getMaxHealth() < 0.5 ? TEXTURE_TRANSITION : TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_IDLE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TRANSITION))
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new ApathyrAnimator("apathyr"))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .addRenderLayer(new LayerApathyrShield())
                        .setShadowRadius(0.6F)
                        .build()
                , renderManager);
    }

    @Override
    public void doRender(EntityApathyr entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity != null) {
            entity.doRender(this.renderManager, x, y, z, entityYaw, partialTicks);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
