package com.dungeon_additions.da.entity.render.flame_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.flame_knight.EntityVolatileSpirit;
import com.dungeon_additions.da.entity.model.flame_dungeon.ModelIncendium;
import com.dungeon_additions.da.entity.model.flame_dungeon.ModelVolatileSpirit;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


import javax.annotation.Nullable;

public class RenderVolatileSpirit extends RenderGeoExtended<EntityVolatileSpirit> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/volatile_spirit.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame/geo.volatile_spirit.json");

    public RenderVolatileSpirit(RenderManager renderManager) {
        super(renderManager, new ModelVolatileSpirit(MODEL_RESLOC, TEXTURE, "volatile_spirit"));
       this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityVolatileSpirit entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityVolatileSpirit currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityVolatileSpirit currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityVolatileSpirit currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityVolatileSpirit currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityVolatileSpirit currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityVolatileSpirit currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityVolatileSpirit currentEntity) {
        return null;
    }
}
