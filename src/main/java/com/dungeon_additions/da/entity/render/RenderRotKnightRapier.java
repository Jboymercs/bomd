package com.dungeon_additions.da.entity.render;

import com.dungeon_additions.da.entity.model.ModelRotKnightRapier;
import com.dungeon_additions.da.entity.render.util.RenderRotExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderRotKnightRapier extends RenderRotExtended<EntityRotKnightRapier> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/rot_knight_r.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.rot_knight_r.json");

    public RenderRotKnightRapier(RenderManager renderManager) {
        super(renderManager, new ModelRotKnightRapier(MODEL_RESLOC, TEXTURE, "ancient_knight_rapier"));
    }

    @Override
    public void doRender(EntityRotKnightRapier entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityRotKnightRapier currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromKnightHand(EntityRotKnightRapier.ROT_KNIGHT_HAND.getFromBoneName(boneName));
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
    protected IBlockState getHeldBlockForBone(String boneName, EntityRotKnightRapier currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityRotKnightRapier currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityRotKnightRapier currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityRotKnightRapier currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityRotKnightRapier currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityRotKnightRapier currentEntity) {
        return null;
    }
}
