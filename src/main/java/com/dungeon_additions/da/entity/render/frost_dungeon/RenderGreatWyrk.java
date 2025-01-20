package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.ITarget;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelGreatWyrk;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class RenderGreatWyrk extends RenderGeoExtended<EntityGreatWyrk> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_wyrk.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.great_wyrk.json");

    public RenderGreatWyrk(RenderManager renderManager) {
        super(renderManager, new ModelGreatWyrk(MODEL_RESLOC, TEXTURE, "great_wyrk"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
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

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityGreatWyrk currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityGreatWyrk currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityGreatWyrk currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityGreatWyrk currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityGreatWyrk currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityGreatWyrk currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityGreatWyrk currentEntity) {
        return null;
    }
}
