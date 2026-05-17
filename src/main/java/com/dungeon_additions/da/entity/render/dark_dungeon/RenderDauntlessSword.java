package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessSword;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDauntlessAOE;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDauntlessSword;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderDauntlessSword extends RenderGeoExtended<EntityDauntlessSword> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_sword.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dauntless_sword.json");

    public RenderDauntlessSword(RenderManager renderManager) {
        super(renderManager, new ModelDauntlessSword(MODEL_RESLOC, TEXTURE, "dauntless_sword"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDauntlessSword currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDauntlessSword currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDauntlessSword currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDauntlessSword currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDauntlessSword currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDauntlessSword currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDauntlessSword currentEntity) {
        return null;
    }
}
