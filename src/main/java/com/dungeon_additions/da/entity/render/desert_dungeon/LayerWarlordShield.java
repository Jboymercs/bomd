package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianWarlord;
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

public class LayerWarlordShield implements AzRenderLayer<UUID, EntityAegyptianWarlord> {

   // private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.aegyptian_warlord.json");
   // private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_warlord.png");

    private static final ResourceLocation TEXTURE_INNER_ORB = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_warlord_shield.png");

    public LayerWarlordShield() {
    }

    @Override
    public void preRender(AzRendererPipelineContext<UUID, EntityAegyptianWarlord> azRendererPipelineContext) {

    }

    @Override
    public void render(AzRendererPipelineContext<UUID, EntityAegyptianWarlord> azRendererPipelineContext) {
        if(azRendererPipelineContext.animatable().isShielded()) {
            this.renderLayer(azRendererPipelineContext);
        }
    }

    @Override
    public void renderForBone(AzRendererPipelineContext<UUID, EntityAegyptianWarlord> azRendererPipelineContext, AzBone azBone) {

    }


    public void renderLayer(AzRendererPipelineContext<UUID, EntityAegyptianWarlord> context) {
        AzRendererPipeline<UUID, EntityAegyptianWarlord> renderPipeline = context.rendererPipeline();
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
            float f = (float) context.animatable().ticksExisted + context.partialTick();
            GlStateManager.translate(f * 0.02F, 0.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
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

    protected RenderType determineRenderType(AzRendererPipelineContext<UUID, EntityAegyptianWarlord> context) {
        AzRendererConfig<UUID, EntityAegyptianWarlord> config = context.rendererPipeline().config();
        ResourceLocation textureLocation = config.textureLocation(context.currentEntity(), context.animatable());
        {
            Entity entity = (Entity) context.animatable();
            boolean isInvisible = entity.isInvisible();
            boolean appearsGlowing = entity.isGlowing();
            boolean isPlayerInvisible = entity.isInvisibleToPlayer(ClientUtils.getClientPlayer());
            if (isInvisible) {
                if (!isPlayerInvisible) {
                    return RenderType.itemEntityTranslucentCull(AutoGlowingTexture.getEmissiveResource(textureLocation));
                } else {
                    return appearsGlowing ? RenderType.outline(AutoGlowingTexture.getEmissiveResource(textureLocation)) : null;
                }
            } else {
                return appearsGlowing ? AutoGlowingTexture.getOutlineRenderType(textureLocation) : AutoGlowingTexture.getRenderType(textureLocation);
            }
        }
    }
}
