package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.EntityTestModel;
import com.dungeon_additions.da.entity.model.ModelRotKnightBoss;
import com.dungeon_additions.da.entity.model.ModelTestModel;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderTestModel extends RenderGeoExtended<EntityTestModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/test_model.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/geo.test_model.json");
    public RenderTestModel(RenderManager renderManager) {
        super(renderManager, new ModelTestModel(MODEL_RESLOC, TEXTURE, "test_model"));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityTestModel currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityTestModel currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityTestModel currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityTestModel currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityTestModel currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityTestModel currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityTestModel currentEntity) {
        return null;
    }
}
