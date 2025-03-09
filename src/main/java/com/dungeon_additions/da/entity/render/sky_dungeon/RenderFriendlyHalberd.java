package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelFriendlyHalberd;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialHalberd;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialHalberd;
import com.dungeon_additions.da.entity.sky_dungeon.friendly.EntityFriendlyHalberd;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderFriendlyHalberd extends RenderGeoExtended<EntityFriendlyHalberd> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/imperial_halberd_friendly.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.imperial_halberd.json");

    public RenderFriendlyHalberd(RenderManager renderManager) {
        super(renderManager, new ModelFriendlyHalberd(MODEL_RESLOC, TEXTURE, "imperial_halberd_friendly"));
    }

    @Override
    public void doRender(EntityFriendlyHalberd entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityFriendlyHalberd currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityFriendlyHalberd currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityFriendlyHalberd currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityFriendlyHalberd currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityFriendlyHalberd currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityFriendlyHalberd currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityFriendlyHalberd currentEntity) {
        return null;
    }
}
