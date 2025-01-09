package com.dungeon_additions.da.entity.render.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelWyrk;
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

public class RenderDraugr extends RenderGeoExtended<EntityDraugr> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.draugr.json");
    public RenderDraugr(RenderManager renderManager) {
        super(renderManager, new ModelDraugr(MODEL_RESLOC, TEXTURE, "frost_draugr"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDraugr currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityDraugr currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDraugr currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDraugr currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDraugr currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDraugr currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDraugr currentEntity) {
        return null;
    }
}
