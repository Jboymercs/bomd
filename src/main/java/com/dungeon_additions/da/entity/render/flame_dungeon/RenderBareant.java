package com.dungeon_additions.da.entity.render.flame_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityBareant;
import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.model.flame_dungeon.ModelBareant;
import com.dungeon_additions.da.entity.model.flame_dungeon.ModelIncendium;
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

public class RenderBareant extends RenderGeoExtended<EntityBareant> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/bareant.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame/geo.bareant.json");

    public RenderBareant(RenderManager renderManager) {
        super(renderManager, new ModelBareant(MODEL_RESLOC, TEXTURE, "bareant"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityBareant currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityBareant currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityBareant currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityBareant currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityBareant currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityBareant currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityBareant currentEntity) {
        return null;
    }
}
