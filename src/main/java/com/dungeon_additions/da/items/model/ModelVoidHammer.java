package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemVoidHammer;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidHammer extends AnimatedGeoModel<ItemVoidHammer> {

    @Override
    public ResourceLocation getModelLocation(ItemVoidHammer object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.void_hammer.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemVoidHammer object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/void_hammer.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemVoidHammer animatable) {
        return null;
    }
}
