package com.dungeon_additions.da.entity.render.sky_dungeon.boss;

import com.dungeon_additions.da.entity.model.sky_dungeon.boss.ModelHighKing;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderHighKing extends RenderGeoExtended<EntityHighKing> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/high_king/geo.high_king.json");

    public RenderHighKing(RenderManager renderManager) {
        super(renderManager, new ModelHighKing(MODEL_RESLOC, TEXTURE, "high_king"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityHighKing currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityHighKing currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityHighKing currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityHighKing currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityHighKing currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityHighKing currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityHighKing currentEntity) {
        return null;
    }
}
