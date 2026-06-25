package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoid;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoidSpawn;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkVoidSpawn;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDauntlessAOE;
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

public class RenderDarkVoidSpawn extends RenderGeoExtended<EntityDarkVoidSpawn> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dark_void_spawn.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dauntless_aoe.json");

    public RenderDarkVoidSpawn(RenderManager renderManager) {
        super(renderManager, new ModelDarkVoidSpawn(MODEL_RESLOC, TEXTURE, "dark_void_spawn"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDarkVoidSpawn currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDarkVoidSpawn currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDarkVoidSpawn currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDarkVoidSpawn currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDarkVoidSpawn currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDarkVoidSpawn currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDarkVoidSpawn currentEntity) {
        return null;
    }
}
