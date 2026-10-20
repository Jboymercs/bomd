package com.dungeon_additions.da.entity.render.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDemonSigil;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntitySpearIncarnate;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelDemonSigil;
import com.dungeon_additions.da.entity.model.dark_dungeon.ModelSpearIncarnate;
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

public class RenderSpearIncarnate extends RenderGeoExtended<EntitySpearIncarnate> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/spear_incarnate.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/dark/geo.spear_incarnate.json");

    public RenderSpearIncarnate(RenderManager renderManager) {
        super(renderManager, new ModelSpearIncarnate(MODEL_RESLOC, TEXTURE, "spear_incarnate"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntitySpearIncarnate currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntitySpearIncarnate currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntitySpearIncarnate currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntitySpearIncarnate currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntitySpearIncarnate currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntitySpearIncarnate currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntitySpearIncarnate currentEntity) {
        return null;
    }
}
