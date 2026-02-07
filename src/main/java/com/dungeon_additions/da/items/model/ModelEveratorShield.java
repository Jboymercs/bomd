package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.shield.ItemEveratorShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelEveratorShield extends AnimatedGeoModel<ItemEveratorShield> {

    @Override
    public ResourceLocation getModelLocation(ItemEveratorShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.everator_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemEveratorShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/everator_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemEveratorShield animatable) {
        return null;
    }
}
