package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelGargoyleMage;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelGargoyleTrident;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGargoyleExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityMageGargoyle;
import com.dungeon_additions.da.entity.sky_dungeon.EntityTridentGargoyle;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderGargoyleMage extends RenderGargoyleExtended<EntityMageGargoyle> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/mage_gargoyle.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.mage_gargoyle.json");

    public RenderGargoyleMage(RenderManager renderManager) {
        super(renderManager, new ModelGargoyleMage(MODEL_RESLOC, TEXTURE, "mage_gargoyle"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityMageGargoyle entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntityMageGargoyle currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityMageGargoyle currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityMageGargoyle currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityMageGargoyle currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityMageGargoyle currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityMageGargoyle currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityMageGargoyle currentEntity) {
        return null;
    }
}
