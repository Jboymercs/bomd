package com.dungeon_additions.da.entity.render.sky_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugr;
import com.dungeon_additions.da.entity.model.sky_dungeon.ModelSkyBolt;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderSkyBolt extends RenderGeoExtended<EntitySkyBolt> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/bolt/lightning_bolt.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.lightning_bolt.json");

    public RenderSkyBolt(RenderManager renderManager) {
        super(renderManager, new ModelSkyBolt(MODEL_RESLOC, TEXTURE, "sky_lightning_bolt"));
      //  this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntitySkyBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderManager.renderEngine.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
        if (entity.getRenderDirection() != null && entity.ticksExisted > 17 ) {
            double scale = 1;
            if(entity.ticksExisted > 20) {
                scale -= 0.11;
            }
            RenderUtil.drawBeam(renderManager, entity.getPositionVector(), entity.getRenderDirection(), new Vec3d(x, y, z), ModColors.YELLOW, entity, partialTicks, new Vec3d(scale * 1.2, 1, scale * 1.2));
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntitySkyBolt currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntitySkyBolt currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntitySkyBolt currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntitySkyBolt currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntitySkyBolt currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntitySkyBolt currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntitySkyBolt currentEntity) {
        return null;
    }
}
