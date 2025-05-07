package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDarkShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDarkShield extends AnimatedGeoModel<ItemDarkShield> {

    @Override
    public ResourceLocation getModelLocation(ItemDarkShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.dark_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDarkShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dark_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDarkShield animatable) {
        return null;
    }
}
