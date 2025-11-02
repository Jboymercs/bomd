package com.dungeon_additions.da.entity.render.trader;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkRoyal;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.model.gaelon_dungeon.ModelApathyr;
import com.dungeon_additions.da.entity.model.trader.ModelMysteriousTrader;
import com.dungeon_additions.da.entity.render.layer.GeoSpecificGlow;
import com.dungeon_additions.da.entity.render.layer.LayerApathyrShield;
import com.dungeon_additions.da.entity.render.util.RenderGeoExtended;
import com.dungeon_additions.da.entity.trader.EntityMysteriousTrader;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class RenderMysteriousTrader extends RenderGeoExtended<EntityMysteriousTrader> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModReference.MOD_ID, "textures/entity/mysterious_trader.png");
    private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/trader/geo.mysterious_trader.json");

    public RenderMysteriousTrader(RenderManager renderManager) {
        super(renderManager, new ModelMysteriousTrader(MODEL_RESLOC, TEXTURE, "mysterious_trader"));
        this.addLayer(new GeoSpecificGlow<>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, EntityMysteriousTrader currentEntity) {
        ItemStack stackInhand = currentEntity.getItemFromRoyalHand(EntityMysteriousTrader.TRADER_HAND.getFromBoneName(boneName));
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
    protected IBlockState getHeldBlockForBone(String boneName, EntityMysteriousTrader currentEntity) {
        return null;
    }

    @Override
    protected void preRenderItem(ItemStack item, String boneName, EntityMysteriousTrader currentEntity) {

    }

    @Override
    protected void preRenderBlock(IBlockState block, String boneName, EntityMysteriousTrader currentEntity) {

    }

    @Override
    protected void postRenderItem(ItemStack item, String boneName, EntityMysteriousTrader currentEntity) {

    }

    @Override
    protected void postRenderBlock(IBlockState block, String boneName, EntityMysteriousTrader currentEntity) {

    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, EntityMysteriousTrader currentEntity) {
        return null;
    }
}
