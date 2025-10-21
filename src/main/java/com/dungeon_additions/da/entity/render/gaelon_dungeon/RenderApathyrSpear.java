package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyrSpear;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelVoidBomb;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBomb;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderApathyrSpear extends RenderGeoExtended<EntityApathyrSpear> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_spear.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.apathyr_spear.json");
    public RenderApathyrSpear(RenderManager renderManager) {
        super(renderManager, new ModelApathyrSpear(MODEL_RESLOC, TEXTURE, "apathyr_spear"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityApathyrSpear currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityApathyrSpear currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityApathyrSpear currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityApathyrSpear currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityApathyrSpear currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityApathyrSpear currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityApathyrSpear currentEntity) {
        return null;
    }
}
