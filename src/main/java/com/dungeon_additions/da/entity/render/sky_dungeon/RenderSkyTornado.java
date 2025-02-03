package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelImperialSword;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelSkyTornado;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityImperialSword;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyTornado;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderSkyTornado extends RenderGeoExtended<EntitySkyTornado> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/sky_tornado.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.sky_tornado.json");

    public RenderSkyTornado(RenderManager renderManager) {
        super(renderManager, new ModelSkyTornado(MODEL_RESLOC, TEXTURE, "sky_tornado"));
    }

    @Override
    public void doRender(EntitySkyTornado entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ItemStack getHeldItemForBone(String boneName, EntitySkyTornado currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntitySkyTornado currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntitySkyTornado currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntitySkyTornado currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntitySkyTornado currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntitySkyTornado currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntitySkyTornado currentEntity) {
        return null;
    }
}
