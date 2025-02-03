package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialHalberd;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialSword;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialSword;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderImperialSword  extends RenderGeoExtended<EntityImperialSword> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/imperial_sword.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.imperial_sword.json");

    public RenderImperialSword(RenderManager renderManager) {
        super(renderManager, new ModelImperialSword(MODEL_RESLOC, TEXTURE, "imperial_sword"));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityImperialSword currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityImperialSword currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityImperialSword currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityImperialSword currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityImperialSword currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityImperialSword currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityImperialSword currentEntity) {
        return null;
    }
}
