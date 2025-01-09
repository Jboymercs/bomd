package com.dungeon_additions.da.entity.render.frost_dungeon;


import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.model.frost_dungeon.ModelDraugrRanger;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;


import javax.annotation.Nullable;

public class RenderDraugrRanger extends RenderDraugrExtended<EntityDraugrRanger> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_ranger.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/frost/geo.draugr_ranger.json");
    public RenderDraugrRanger(RenderManager renderManager) {
        super(renderManager, new ModelDraugrRanger(MODEL_RESLOC, TEXTURE, "frost_draugr_ranger"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityDraugrRanger currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromKnightHand(EntityDraugrRanger.RANGER_HAND.getFromBoneName(boneName));
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
    protected IBlockState getHeldBlockForBone(String boneName, EntityDraugrRanger currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityDraugrRanger currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityDraugrRanger currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityDraugrRanger currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityDraugrRanger currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityDraugrRanger currentEntity) {
        return null;
    }
}
