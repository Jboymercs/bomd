package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.model.sky_dungeon.ModelDragonSpecial;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelKingHolyAOE;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyAOE;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderKingHolyAOE extends RenderGeoExtended<EntityKingHolyAOE> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/king_holy_aoe.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.king_holy_aoe.json");
    public RenderKingHolyAOE(RenderManager renderManager) {
        super(renderManager, new ModelKingHolyAOE(MODEL_RESLOC, TEXTURE, "king_holy_aoe"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityKingHolyAOE currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityKingHolyAOE currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityKingHolyAOE currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityKingHolyAOE currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityKingHolyAOE currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityKingHolyAOE currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityKingHolyAOE currentEntity) {
        return null;
    }
}
