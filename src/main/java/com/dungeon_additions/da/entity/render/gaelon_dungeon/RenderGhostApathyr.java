package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityApathyrGhost;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyr;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelGhostApathyr;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderGhostApathyr extends RenderGeoExtended<EntityApathyrGhost> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_ghost.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.apathyr.json");
    public RenderGhostApathyr(RenderManager renderManager) {
        super(renderManager, new ModelGhostApathyr(MODEL_RESLOC, TEXTURE, "apathyr_ghost"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityApathyrGhost entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityApathyrGhost currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityApathyrGhost currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityApathyrGhost currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityApathyrGhost currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityApathyrGhost currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityApathyrGhost currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityApathyrGhost currentEntity) {
        return null;
    }
}
