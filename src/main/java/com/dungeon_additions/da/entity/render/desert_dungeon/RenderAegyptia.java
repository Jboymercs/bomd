package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.EntityAegyptia;
import com.dungeon_additions.da.entity.model.desert_dungeon.ModelAegyptia;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderAegyptia extends RenderGeoExtended<EntityAegyptia> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.aegyptia.json");


    public RenderAegyptia(RenderManager renderManager) {
        super(renderManager, new ModelAegyptia(MODEL_RESLOC, TEXTURE, "aegyptia"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityAegyptia currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityAegyptia currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityAegyptia currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityAegyptia currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityAegyptia currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityAegyptia currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityAegyptia currentEntity) {
        return null;
    }
}
