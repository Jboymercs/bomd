package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemImperialHalberd;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelImperialHalberdItem extends AnimatedGeoModel<ItemImperialHalberd> {
    @Override
    public ResourceLocation getModelLocation(ItemImperialHalberd object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.imperial_halberd.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemImperialHalberd object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/imperial_halberd.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemImperialHalberd animatable) {
        return null;
    }
}
