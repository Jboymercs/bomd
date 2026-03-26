package com.dungeon_additions.da.entity.render.generic;

import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.generic.EntityRallyFlag;
import com.dungeon_additions.da.entity.model.generic.ModelDelayedExplosion;
import com.dungeon_additions.da.entity.model.generic.ModelRallyFlag;
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

public class RenderRallyFlag extends RenderGeoExtended<EntityRallyFlag> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/rally_flag.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/generic/geo.rally_flag.json");
    public RenderRallyFlag(RenderManager renderManager) {
        super(renderManager, new ModelRallyFlag(MODEL_RESLOC, TEXTURE, "rally_flag"));

        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityRallyFlag currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityRallyFlag currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityRallyFlag currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityRallyFlag currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityRallyFlag currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityRallyFlag currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityRallyFlag currentEntity) {
        return null;
    }
}
