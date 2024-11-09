package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.ModelFlameKnight;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerFlameHat;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoCube;

import javax.annotation.Nullable;

public class RenderFlameKnight extends RenderGeoExtended<EntityFlameKnight> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame_knight/geo.flame_knight.json");
    public RenderFlameKnight(RenderManager renderManager) {
        super(renderManager, new ModelFlameKnight(MODEL_RESLOC, TEXTURE, "flame_knight"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.addLayer(new LayerFlameHat(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityFlameKnight entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityFlameKnight currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromKnightHand(EntityFlameKnight.KNIGHT_HAND.getFromBoneName(boneName));
        if(stackInhand != null) {
             return stackInhand;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityFlameKnight currentEntity) {
        Optional<IBlockState> optional = currentEntity.getBlockFromHead(EntityFlameKnight.HEAD_REPLACEMENT.getFromBoneName(boneName));
        if(optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityFlameKnight currentEntity) {
    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityFlameKnight currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityFlameKnight currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityFlameKnight currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityFlameKnight currentEntity) {
        return null;
    }

    @Override
    public void renderCube(BufferBuilder builder, GeoCube cube, float red, float green, float blue, float alpha) {
        super.renderCube(builder, cube, red, green, blue, alpha);
    }

    @Override
    public void renderAfter(EntityFlameKnight animatable, float ticks, float red, float green, float blue, float partialTicks) {
        super.renderAfter(animatable, ticks, red, green, blue, partialTicks);
    }

    @Override
    public Color getRenderColor(EntityFlameKnight animatable, float partialTicks) {
        return super.getRenderColor(animatable, partialTicks);
    }
}
