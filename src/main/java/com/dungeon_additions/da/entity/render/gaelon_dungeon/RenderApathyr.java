package com.dungeon_additions.da.entity.render.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyr;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelReAnimate;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.entity.render.layer.LayerObsidilithShield;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderApathyr extends RenderGeoExtended<EntityApathyr> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/gaelon/geo.apathyr.json");

    public RenderApathyr(RenderManager renderManager) {
        super(renderManager, new ModelApathyr(MODEL_RESLOC, TEXTURE, "apathyr"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
        this.addLayer(new LayerApathyrShield(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Override
    public void doRender(EntityApathyr entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity != null) {
            entity.doRender(this.renderManager, x, y, z, entityYaw, partialTicks);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityApathyr currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityApathyr currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityApathyr currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityApathyr currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityApathyr currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityApathyr currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityApathyr currentEntity) {
        return null;
    }
}
