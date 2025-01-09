package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelIcSpike;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelWyrk;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderIceSpike extends RenderGeoExtended<EntityIcicleSpike> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/ice_spike_large.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.ice_spike.json");
    public RenderIceSpike(RenderManager renderManager) {
        super(renderManager, new ModelIcSpike(MODEL_RESLOC, TEXTURE, "ice_spike"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityIcicleSpike currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityIcicleSpike currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityIcicleSpike currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityIcicleSpike currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityIcicleSpike currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityIcicleSpike currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityIcicleSpike currentEntity) {
        return null;
    }
}
