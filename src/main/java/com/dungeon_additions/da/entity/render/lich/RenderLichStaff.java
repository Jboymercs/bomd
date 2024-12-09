package com.dungeon_additions.da.entity.render.lich;

import com.dungeon_additions.da.entity.model.lich.ModelLichStaff;
import com.dungeon_additions.da.entity.model.lich.ModelNightLich;
import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
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

public class RenderLichStaff extends RenderGeoExtended<EntityLichStaffAOE> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/lich_staff.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/night_lich/geo.lich_staff.json");


    public RenderLichStaff(RenderManager renderManager) {
        super(renderManager, new ModelLichStaff(MODEL_RESLOC, TEXTURE, "lich_staff"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityLichStaffAOE currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityLichStaffAOE currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityLichStaffAOE currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityLichStaffAOE currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityLichStaffAOE currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityLichStaffAOE currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityLichStaffAOE currentEntity) {
        return null;
    }
}
