package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.model.ModelRotKnightBoss;
import com.dungeon_additions.da.entity.model.ModelRotKnightRapier;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderRotKnightBoss extends RenderGeoExtended<EntityRotKnightBoss> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/fallen.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.fallen.json");
    public RenderRotKnightBoss(RenderManager renderManager) {
        super(renderManager, new ModelRotKnightBoss(MODEL_RESLOC, TEXTURE, "ancient_fallen"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityRotKnightBoss currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityRotKnightBoss currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityRotKnightBoss currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityRotKnightBoss currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityRotKnightBoss currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityRotKnightBoss currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityRotKnightBoss currentEntity) {
        return null;
    }
}
