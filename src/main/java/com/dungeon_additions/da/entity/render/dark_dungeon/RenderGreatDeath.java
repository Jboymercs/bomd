package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkDriftDevil;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelGreatDeath;
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

public class RenderGreatDeath extends RenderGeoExtended<EntityGreatDeath> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_death.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.great_death.json");

    public RenderGreatDeath(RenderManager renderManager) {
        super(renderManager, new ModelGreatDeath(MODEL_RESLOC, TEXTURE, "great_death"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityGreatDeath currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityGreatDeath currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityGreatDeath currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityGreatDeath currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityGreatDeath currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityGreatDeath currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityGreatDeath currentEntity) {
        return null;
    }
}
