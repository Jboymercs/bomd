package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ITarget;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.RenderUtil;
import mod.azure.azurelib.render.entity.AzEntityRenderer;
import mod.azure.azurelib.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.render.layer.AzAutoGlowingLayer;
import mod.azure.azurelib.render.vertex.RenderType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderGreatWyrk extends AzEntityRenderer<EntityGreatWyrk> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_wyrk.png");
    private static final ResourceLocation TEXTURE_TWO = new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_wyrk_hurt.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.great_wyrk.json");

    public RenderGreatWyrk(RenderManager renderManager) {
        super(AzEntityRendererConfig.<EntityGreatWyrk>builder($ -> MODEL_RESLOC, entity ->
                entity.hurtTime > 0 || entity.isSummonBoss() ? TEXTURE_TWO :TEXTURE)
                        .setRenderType(RenderType.entityTranslucent(TEXTURE))
                        .setRenderType(RenderType.entityTranslucent(TEXTURE_TWO))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setRenderEntry(contextPipeline -> {

                            contextPipeline.animatable().updateAnimations();
                            return contextPipeline;
                        })

                        .setAnimatorProvider(() -> new DaEntityAnimator<>("great_wyrk", false, false))
                        .addRenderLayer(new AzAutoGlowingLayer<>())
                        .setShadowRadius(0.4F)
                        .build()
                , renderManager);
    }

    @Override
    public void doRender(EntityGreatWyrk entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.AZURE, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Override
    public boolean shouldRender(@Nonnull EntityGreatWyrk livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
        {
            return true;
        }

        Optional<Vec3d> optional = ((ITarget) livingEntity).getTarget();
        if(optional.isPresent()) {
            Vec3d end = optional.get();
            Vec3d start = livingEntity.getPositionEyes(1);
            return camera.isBoundingBoxInFrustum(ModUtils.makeBox(start, end));
        }

        return false;
    }
}
