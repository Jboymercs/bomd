package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.EntityEverchosen;
import com.dungeon_additions.da.entity.dark_dungeon.EntityEverchosenHammer;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelEverchosenHammer;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelEverchosenSword;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderEverChosenHammer extends RenderGeoExtended<EntityEverchosenHammer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/everchosen.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.everchosen.json");

    public RenderEverChosenHammer(RenderManager renderManager) {
        super(renderManager, new ModelEverchosenHammer(MODEL_RESLOC, TEXTURE, "everchosen_hammer"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }
    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityEverchosenHammer currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityEverchosenHammer currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityEverchosenHammer currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityEverchosenHammer currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityEverchosenHammer currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityEverchosenHammer currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityEverchosenHammer currentEntity) {
        return null;
    }
}
