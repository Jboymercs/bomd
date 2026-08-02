package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.model.ModelChevalier;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityChevalier;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderChevalier extends RenderGeoExtended<EntityChevalier> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/chevalier.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.chevalier.json");

    public RenderChevalier(RenderManager renderManager) {
        super(renderManager, new ModelChevalier(MODEL_RESLOC, TEXTURE, "chevalier"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityChevalier currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityChevalier currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityChevalier currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityChevalier currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityChevalier currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityChevalier currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityChevalier currentEntity) {
        return null;
    }
}
