package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityApathyrEye;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyrEye;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderApathyrEye extends RenderGeoExtended<EntityApathyrEye> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_eye.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.apathyr_eye.json");

    public RenderApathyrEye(RenderManager renderManager) {
        super(renderManager, new ModelApathyrEye(MODEL_RESLOC, TEXTURE, "apathyr_eye"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityApathyrEye currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityApathyrEye currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityApathyrEye currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityApathyrEye currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityApathyrEye currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityApathyrEye currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityApathyrEye currentEntity) {
        return null;
    }
}
