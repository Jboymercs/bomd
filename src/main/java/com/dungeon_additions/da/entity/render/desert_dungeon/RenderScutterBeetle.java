package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.entity.model.desert_dungeon.ModelScutterBeetle;
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

public class RenderScutterBeetle extends RenderGeoExtended<EntityScutterBeetle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/scutter_beetle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.scutter_beetle.json");
    public RenderScutterBeetle(RenderManager renderManager) {
        super(renderManager, new ModelScutterBeetle(MODEL_RESLOC, TEXTURE, "scutter_beetle"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityScutterBeetle currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityScutterBeetle currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityScutterBeetle currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityScutterBeetle currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityScutterBeetle currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityScutterBeetle currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityScutterBeetle currentEntity) {
        return null;
    }
}
