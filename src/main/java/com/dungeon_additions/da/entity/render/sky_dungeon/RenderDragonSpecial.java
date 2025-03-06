package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelDragonSpecial;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelGargoyleLazer;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGargoyleExtended;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityGargoyleLazer;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderDragonSpecial extends RenderGeoExtended<EntityDragonSpecial> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/dragon_special.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.dragon_special.json");

    public RenderDragonSpecial(RenderManager renderManager) {
        super(renderManager, new ModelDragonSpecial(MODEL_RESLOC, TEXTURE, "dragon_special"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDragonSpecial currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDragonSpecial currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDragonSpecial currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDragonSpecial currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDragonSpecial currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDragonSpecial currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDragonSpecial currentEntity) {
        return null;
    }
}
