package com.dungeon_additions.da.entity.render.sky_dungeon.boss;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialHalberd;
import com.dungeon_additions.da.entity.model.sky_dungeon.boss.ModelHighKingDrake;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderHighKingDrake extends RenderGeoExtended<EntityHighKingDrake> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king_drake.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/high_king/geo.high_king_drake.json");

    public RenderHighKingDrake(RenderManager renderManager) {
        super(renderManager, new ModelHighKingDrake(MODEL_RESLOC, TEXTURE, "high_king_drake"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityHighKingDrake currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityHighKingDrake currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityHighKingDrake currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityHighKingDrake currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityHighKingDrake currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityHighKingDrake currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityHighKingDrake currentEntity) {
        return null;
    }
}
