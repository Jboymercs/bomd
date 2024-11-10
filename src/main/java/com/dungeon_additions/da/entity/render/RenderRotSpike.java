package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.model.ModelRotKnightBoss;
import com.dungeon_additions.da.entity.model.ModelRotSpike;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderRotSpike extends RenderGeoExtended<EntityRotSpike> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_spike.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.rot_spike.json");
    public RenderRotSpike(RenderManager renderManager) {
        super(renderManager, new ModelRotSpike(MODEL_RESLOC, TEXTURE, "rot_spike"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityRotSpike currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityRotSpike currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityRotSpike currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityRotSpike currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityRotSpike currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityRotSpike currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityRotSpike currentEntity) {
        return null;
    }
}
