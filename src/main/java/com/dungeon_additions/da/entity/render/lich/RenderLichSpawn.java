package com.dungeon_additions.da.entity.render.lich;

import com.dungeon_additions.da.entity.model.lich.ModelLichSpawn;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderLichSpawn extends RenderGeoExtended<EntityLichSpawn> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/spawn/lich_spawn_4.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/night_lich/geo.lich_spawn.json");
    public RenderLichSpawn(RenderManager renderManager) {
        super(renderManager, new ModelLichSpawn(MODEL_RESLOC, TEXTURE, "lich_spawn"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityLichSpawn currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityLichSpawn currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityLichSpawn currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityLichSpawn currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityLichSpawn currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityLichSpawn currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityLichSpawn currentEntity) {
        return null;
    }
}
