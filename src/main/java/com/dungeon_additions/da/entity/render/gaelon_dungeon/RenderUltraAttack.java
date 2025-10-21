package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityUltraAttack;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyr;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelUltraAttack;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.entity.render.layer.LayerUltraAttack;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderUltraAttack extends RenderGeoExtended<EntityUltraAttack> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/ultra_attack.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.ultra_attack.json");

    public RenderUltraAttack(RenderManager renderManager) {
        super(renderManager, new ModelUltraAttack(MODEL_RESLOC, TEXTURE, "ultra_attack"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.addLayer(new LayerUltraAttack(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityUltraAttack currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityUltraAttack currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityUltraAttack currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityUltraAttack currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityUltraAttack currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityUltraAttack currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityUltraAttack currentEntity) {
        return null;
    }
}
