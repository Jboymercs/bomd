package com.dungeon_additions.da.entity.render.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianWarlord;
import com.dungeon_additions.da.entity.model.desert_dungeon.ModelAegyptia;
import com.dungeon_additions.da.entity.model.desert_dungeon.ModelAegyptianWarlord;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.util.RenderGargoyleExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderAegyptianWarlord extends RenderGargoyleExtended<EntityAegyptianWarlord> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_warlord.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/desert/geo.aegyptian_warlord.json");

    public RenderAegyptianWarlord(RenderManager renderManager) {
        super(renderManager, new ModelAegyptianWarlord(MODEL_RESLOC, TEXTURE, "aegyptian_warlord"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityAegyptianWarlord currentEntity) {
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        return null;
    }

    @Nullable
    @Override
    protected IBlockState getHeldBlockForBone(String boneName, EntityAegyptianWarlord currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityAegyptianWarlord currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityAegyptianWarlord currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityAegyptianWarlord currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityAegyptianWarlord currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityAegyptianWarlord currentEntity) {
        return null;
    }
}
