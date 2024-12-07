package com.dungeon_additions.da.entity.render.lich;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.ModelFlameKnight;
import com.dungeon_additions.da.entity.model.lich.ModelNightLich;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
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

public class RenderNightLich extends RenderGeoExtended<EntityNightLich> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/night_lich.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/night_lich/geo.night_lich.json");


    public RenderNightLich(RenderManager renderManager) {
        super(renderManager, new ModelNightLich(MODEL_RESLOC, TEXTURE, "night_lich"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityNightLich currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityNightLich currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityNightLich currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityNightLich currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityNightLich currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityNightLich currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityNightLich currentEntity) {
        return null;
    }
}
