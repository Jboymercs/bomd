package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoid;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoidSpawn;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkVoid;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkVoidSpawn;
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

public class RenderDarkVoid extends RenderGeoExtended<EntityDarkVoid> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dark_orb.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dark_void.json");

    public RenderDarkVoid(RenderManager renderManager) {
        super(renderManager, new ModelDarkVoid(MODEL_RESLOC, TEXTURE, "dark_orb"));
       // this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDarkVoid currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDarkVoid currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDarkVoid currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDarkVoid currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDarkVoid currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDarkVoid currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDarkVoid currentEntity) {
        return null;
    }
}
