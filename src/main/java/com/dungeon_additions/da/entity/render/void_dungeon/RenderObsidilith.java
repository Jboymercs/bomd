package com.dungeon_additions.da.entity.render.void_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.ModelFlameKnight;
import com.dungeon_additions.da.entity.model.void_dungeon.ModelObsidilith;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerObsidilithShield;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderObsidilith extends RenderGeoExtended<EntityObsidilith> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/obsidilith.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.obsidilith.json");

    public RenderObsidilith(RenderManager renderManager) {
        super(renderManager, new ModelObsidilith(MODEL_RESLOC, TEXTURE, "obsidilith"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.addLayer(new LayerObsidilithShield(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityObsidilith currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityObsidilith currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityObsidilith currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityObsidilith currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityObsidilith currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityObsidilith currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityObsidilith currentEntity) {
        return null;
    }
}
