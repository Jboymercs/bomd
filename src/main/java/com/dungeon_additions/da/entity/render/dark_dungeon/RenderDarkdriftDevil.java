package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkDriftDevil;
import com.dungeon_additions.da.entity.render.dark_dungeon.layer.RenderLayerDemonBuff;
import com.dungeon_additions.da.entity.render.dark_dungeon.layer.RenderLayerDemonShield;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderDarkdriftDevil  extends RenderGeoExtended<EntityDarkdriftDevil> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/darkdrift_devil.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.darkdrift_devil.json");

    public RenderDarkdriftDevil(RenderManager renderManager) {
        super(renderManager, new ModelDarkDriftDevil(MODEL_RESLOC, TEXTURE, "darkdrift_devil"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        //buffs
        this.addLayer(new RenderLayerDemonShield(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.addLayer(new RenderLayerDemonBuff(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDarkdriftDevil currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDarkdriftDevil currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDarkdriftDevil currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDarkdriftDevil currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDarkdriftDevil currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDarkdriftDevil currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDarkdriftDevil currentEntity) {
        return null;
    }
}
