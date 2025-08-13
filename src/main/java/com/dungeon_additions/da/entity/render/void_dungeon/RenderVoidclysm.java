package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelObsidilith;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelVoidclysm;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
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
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderVoidclysm extends RenderGeoExtended<EntityVoidiclysm> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/voidclysm.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.voidclysm.json");

    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/beam/voidclysm_beam.png");
    public RenderVoidclysm(RenderManager renderManager) {
        super(renderManager, new ModelVoidclysm(MODEL_RESLOC, TEXTURE, "voidclysm"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityVoidiclysm entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //Help from the Gauntlet in Maelstrom for Rendering the Lazer
        if (entity.renderLazerPos != null) {
            // This sort of jenky way of binding the wrong texture to the original guardian beam creates quite a nice particle beam visual
            renderManager.renderEngine.bindTexture(BEAM_TEXTURE);
            // We must interpolate between positions to make the move smoothly
            Vec3d interpolatedPos = entity.renderLazerPos.subtract(entity.prevRenderLazerPos).scale(partialTicks).add(entity.prevRenderLazerPos);
            RenderUtil.drawBeam(renderManager, entity.getPositionEyes(1), interpolatedPos, new Vec3d(x, y, z), ModColors.LIGHT_PURPLE, entity, partialTicks);
        }
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityVoidiclysm currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityVoidiclysm currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityVoidiclysm currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityVoidiclysm currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityVoidiclysm currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityVoidiclysm currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityVoidiclysm currentEntity) {
        return null;
    }
}
