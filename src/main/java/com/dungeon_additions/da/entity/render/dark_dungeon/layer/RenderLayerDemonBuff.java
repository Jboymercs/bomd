package com.dungeon_additions.da.entity.render.dark_dungeon.layer;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.render.layer.RenderAbstractLayer;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.glowLayer.EmissiveLighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.function.Function;

public class RenderLayerDemonBuff extends RenderAbstractLayer<EntityDarkdriftDevil> {

    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.darkdrift_devil.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/darkdrift_devil.png");

    private static final ResourceLocation TEXTURE_INNER_ORB = new ResourceLocation(ModReference.MOD_ID, "textures/entity/demon_buff.png");

    public RenderLayerDemonBuff(GeoEntityRenderer<EntityDarkdriftDevil> renderer, Function<EntityDarkdriftDevil, ResourceLocation> funcGetCurrentTexture, Function<EntityDarkdriftDevil, ResourceLocation> funcGetCurrentModel) {
        super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
    }


    @Override
    public void render(EntityDarkdriftDevil entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
        if(entitylivingbaseIn.isDemonBuff()) {
            RenderManager manager = Minecraft.getMinecraft().getRenderManager();

            EmissiveLighting.preEmissiveTextureRendering();
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TEXTURE);
            // GlStateManager.rotate(0.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
            entityRenderer.render(entityRenderer.getGeoModelProvider().getModel(MODEL_RESLOC), entitylivingbaseIn, partialTicks, (float) renderColor.getRed() / 255f, (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getGreen() / 255f, (float) renderColor.getAlpha() / 255);
            GlStateManager.popMatrix();

            //  GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TEXTURE_INNER_ORB);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            GlStateManager.translate(0.0F, f * 0.001F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableCull();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            entityRenderer.render(entityRenderer.getGeoModelProvider().getModel(MODEL_RESLOC), entitylivingbaseIn, partialTicks, (float) renderColor.getRed() / 255f, (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getGreen() / 255f, (float) renderColor.getAlpha() / 255);
            this.reRenderCurrentModelInRenderer(entitylivingbaseIn, partialTicks, renderColor);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            //    GlStateManager.popMatrix();

            EmissiveLighting.postEmissiveTextureRendering();

        }
    }


    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
