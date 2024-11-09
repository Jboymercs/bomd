package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.model.ModelPyre;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderPyre extends RenderGeoExtended<EntityPyre> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/pyre/geo.pyre.json");
    public RenderPyre(RenderManager renderManager) {
        super(renderManager, new ModelPyre(MODEL_RESLOC, TEXTURE, "nether_pyre"));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityPyre currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityPyre currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityPyre currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityPyre currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityPyre currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityPyre currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityPyre currentEntity) {
        return null;
    }
}
