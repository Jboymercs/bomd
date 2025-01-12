package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugrElite;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderDraugrElite extends RenderGeoExtended<EntityEliteDraugr> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_elite.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.draugr_elite.json");
    public RenderDraugrElite(RenderManager renderManager) {
        super(renderManager, new ModelDraugrElite(MODEL_RESLOC, TEXTURE, "frost_draugr_elite"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityEliteDraugr currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEliteDraugr currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEliteDraugr currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEliteDraugr currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEliteDraugr currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEliteDraugr currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEliteDraugr currentEntity) {
        return null;
    }
}
