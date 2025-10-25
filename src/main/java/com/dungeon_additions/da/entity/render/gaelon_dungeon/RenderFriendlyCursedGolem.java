package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityCursedSentinel;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityFriendlyCursedGolem;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelCursedSentinel;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelFriendlySentinel;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderFriendlyCursedGolem extends RenderGeoExtended<EntityFriendlyCursedGolem> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_friendly.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.cursed_golem.json");

    public RenderFriendlyCursedGolem(RenderManager renderManager) {
        super(renderManager, new ModelFriendlySentinel(MODEL_RESLOC, TEXTURE, "cursed_sentinel_friendly"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityFriendlyCursedGolem entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityFriendlyCursedGolem currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityFriendlyCursedGolem currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityFriendlyCursedGolem currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityFriendlyCursedGolem currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityFriendlyCursedGolem currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityFriendlyCursedGolem currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityFriendlyCursedGolem currentEntity) {
        return null;
    }
}
