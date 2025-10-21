package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.flame_knight.EntityVolatileSpirit;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelReAnimate;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGargoyleExtended;
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

public class RenderReAnimate extends RenderGeoExtended<EntityReAnimate> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/reanimate.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.reanimate.json");

    public RenderReAnimate(RenderManager renderManager) {
        super(renderManager, new ModelReAnimate(MODEL_RESLOC, TEXTURE, "reanimate"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityReAnimate entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityReAnimate currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityReAnimate currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityReAnimate currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityReAnimate currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityReAnimate currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityReAnimate currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityReAnimate currentEntity) {
        return null;
    }
}
