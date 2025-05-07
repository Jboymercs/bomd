package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkSorcerer;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkRoyal;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDarkSorcerer;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderDarkRoyal extends RenderGeoExtended<EntityDarkRoyal> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/royal/dark_royal.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.dark_royal.json");

    public RenderDarkRoyal(RenderManager renderManager) {
        super(renderManager, new ModelDarkRoyal(MODEL_RESLOC, TEXTURE, "dark_royal"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDarkRoyal currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromRoyalHand(EntityDarkRoyal.ROYAL_HAND.getFromBoneName(boneName));
        if(stackInhand != null) {
            return stackInhand;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDarkRoyal currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDarkRoyal currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDarkRoyal currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDarkRoyal currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDarkRoyal currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDarkRoyal currentEntity) {
        return null;
    }
}
