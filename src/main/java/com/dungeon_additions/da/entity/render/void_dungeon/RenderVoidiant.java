package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelVoidiant;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiant;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RenderVoidiant extends RenderGeoExtended<EntityVoidiant> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/voidiant.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.voidiant.json");


    public RenderVoidiant(RenderManager renderManager) {
        super(renderManager, new ModelVoidiant(MODEL_RESLOC, TEXTURE, "voidiant"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityVoidiant entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.WHITE, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }


    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityVoidiant currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityVoidiant currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityVoidiant currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityVoidiant currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityVoidiant currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityVoidiant currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityVoidiant currentEntity) {
        return null;
    }
}
