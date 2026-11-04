package com.dungeon_additions.da.entity.render.layer;

import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianColossus;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.glowLayer.EmissiveLighting;
import mod.azure.azurelib.cache.texture.AutoGlowingTexture;
import mod.azure.azurelib.model.AzBone;
import mod.azure.azurelib.render.AzRendererConfig;
import mod.azure.azurelib.render.AzRendererPipeline;
import mod.azure.azurelib.render.AzRendererPipelineContext;
import mod.azure.azurelib.render.layer.AzRenderLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import mod.azure.azurelib.render.vertex.VertexConsumer;
import mod.azure.azurelib.util.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;
import java.util.function.Function;

public class LayerFlameHat implements AzRenderLayer<UUID, EntityFlameKnight> {


   // private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame_knight/geo.flame_knight.json");
   // private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight_flame.png");

    private static final ResourceLocation TEXTURE_INNER_ORB = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight_flame_layer.png");

    public LayerFlameHat() {

    }

    @Override
    public void preRender(AzRendererPipelineContext<UUID, EntityFlameKnight> azRendererPipelineContext) {

    }

    @Override
    public void render(AzRendererPipelineContext<UUID, EntityFlameKnight> azRendererPipelineContext) {
        if(azRendererPipelineContext.animatable().isLostHead() && !azRendererPipelineContext.animatable().isNotCurrentlyInDeath) {
            this.renderLayer(azRendererPipelineContext);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<UUID, EntityFlameKnight> azRendererPipelineContext, AzBone azBone) {

    }

    public void renderLayer(AzRendererPipelineContext<UUID, EntityFlameKnight> context) {
        AzRendererPipeline<UUID, EntityFlameKnight> renderPipeline = context.rendererPipeline();
        RenderType prevRenderType = context.renderType();
        VertexConsumer prevVertexConsumer = context.vertexConsumer();
        RenderType renderType = this.determineRenderType(context);
        int prevPackedLight = context.packedLight();
        float prevRed = context.red();
        float prevGreen = context.green();
        float prevBlue = context.blue();
        float prevAlpha = context.alpha();

        if(renderType != null) {
            EmissiveLighting.preEmissiveTextureRendering();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(TEXTURE_INNER_ORB);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            //

            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableCull();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            //
            context.setRenderType(renderType);
            context.setPackedLight(240);
            context.setVertexConsumer(context.multiBufferSource().getBuffer(renderType));
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            EmissiveLighting.postEmissiveTextureRendering();
        }

        context.setRenderType(prevRenderType);
        context.setVertexConsumer(prevVertexConsumer);
        context.setPackedLight(prevPackedLight);
        context.setRed(prevRed);
        context.setGreen(prevGreen);
        context.setBlue(prevBlue);
        context.setAlpha(prevAlpha);
    }

    protected RenderType determineRenderType(AzRendererPipelineContext<UUID, EntityFlameKnight> context) {
        AzRendererConfig<UUID, EntityFlameKnight> config = context.rendererPipeline().config();
        ResourceLocation textureLocation = config.textureLocation(context.currentEntity(), context.animatable());
        {
            Entity entity = (Entity) context.animatable();
            boolean isInvisible = entity.isInvisible();
            boolean appearsGlowing = entity.isGlowing();
            boolean isPlayerInvisible = entity.isInvisibleToPlayer(ClientUtils.getClientPlayer());
            if (isInvisible) {
                if (!isPlayerInvisible) {
                    return RenderType.itemEntityTranslucentCull(mod.azure.azurelib.cache.texture.AutoGlowingTexture.getEmissiveResource(textureLocation));
                } else {
                    return appearsGlowing ? RenderType.outline(mod.azure.azurelib.cache.texture.AutoGlowingTexture.getEmissiveResource(textureLocation)) : null;
                }
            } else {
                return appearsGlowing ? mod.azure.azurelib.cache.texture.AutoGlowingTexture.getOutlineRenderType(textureLocation) : AutoGlowingTexture.getRenderType(textureLocation);
            }
        }
    }
}
