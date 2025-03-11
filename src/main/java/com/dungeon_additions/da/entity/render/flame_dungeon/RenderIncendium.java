package com.dungeon_additions.da.entity.render.flame_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.flame_dungeon.ModelIncendium;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugr;
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

public class RenderIncendium extends RenderGeoExtended<EntityIncendium> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/incendium.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame/geo.incendium.json");

    public RenderIncendium(RenderManager renderManager) {
        super(renderManager, new ModelIncendium(MODEL_RESLOC, TEXTURE, "incendium"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityIncendium currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityIncendium currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityIncendium currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityIncendium currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityIncendium currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityIncendium currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityIncendium currentEntity) {
        return null;
    }
}
