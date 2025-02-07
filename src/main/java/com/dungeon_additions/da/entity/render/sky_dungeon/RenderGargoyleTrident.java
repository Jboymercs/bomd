package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelGargoyleTrident;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialHalberd;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGargoyleExtended;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderGargoyleTrident extends RenderGargoyleExtended<EntityTridentGargoyle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/trident_gargoyle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.trident_gargoyle.json");
    public RenderGargoyleTrident(RenderManager renderManager) {
        super(renderManager, new ModelGargoyleTrident(MODEL_RESLOC, TEXTURE, "trident_gargoyle"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityTridentGargoyle currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromGargoyleHand(EntityTridentGargoyle.GARGOYLE_HAND.getFromBoneName(boneName));
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
    protected IBlockState getHeldBlockForBone(String boneName, EntityTridentGargoyle currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityTridentGargoyle currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityTridentGargoyle currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityTridentGargoyle currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityTridentGargoyle currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityTridentGargoyle currentEntity) {
        return null;
    }
}
