package com.dungeon_additions.da.entity.render.layer;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.glowLayer.AutoGlowingTexture;
import com.dungeon_additions.da.util.glowLayer.EmissiveLighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.function.Function;

public class LayerFlameHat extends RenderAbstractLayer<EntityFlameKnight> {


    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame_knight/geo.flame_knight.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight_flame.png");

    private static final ResourceLocation TEXTURE_INNER_ORB = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight_flame_layer.png");

    public LayerFlameHat(GeoEntityRenderer<EntityFlameKnight> renderer, Function<EntityFlameKnight, ResourceLocation> funcGetCurrentTexture, Function<EntityFlameKnight, ResourceLocation> funcGetCurrentModel) {
        super(renderer, funcGetCurrentTexture, funcGetCurrentModel);
    }


    @Override
    public void render(EntityFlameKnight entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color renderColor) {
        if(entitylivingbaseIn.isLostHead() && !entitylivingbaseIn.isNotCurrentlyInDeath) {
            RenderManager manager = Minecraft.getMinecraft().getRenderManager();

            EmissiveLighting.preEmissiveTextureRendering();
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TEXTURE);
           // GlStateManager.rotate(0.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
            entityRenderer.render(entityRenderer.getGeoModelProvider().getModel(MODEL_RESLOC), entitylivingbaseIn, partialTicks, (float) renderColor.getRed() / 255f, (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getGreen() / 255f, (float) renderColor.getAlpha() / 255);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TEXTURE_INNER_ORB);
           // GlStateManager.rotate(0.0F - manager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, 1.0F);
            entityRenderer.render(entityRenderer.getGeoModelProvider().getModel(MODEL_RESLOC), entitylivingbaseIn, partialTicks, (float) renderColor.getRed() / 255f, (float) renderColor.getBlue() / 255f,
                    (float) renderColor.getGreen() / 255f, (float) renderColor.getAlpha() / 255);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
            GlStateManager.popMatrix();

            this.reRenderCurrentModelInRenderer(entitylivingbaseIn, partialTicks, renderColor);

            EmissiveLighting.postEmissiveTextureRendering();

        }
    }




    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
