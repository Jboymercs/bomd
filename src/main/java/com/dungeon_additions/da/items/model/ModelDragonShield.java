package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDragonShield;
import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDragonShield extends AnimatedGeoModel<ItemDragonShield> {
    @Override
    public ResourceLocation getModelLocation(ItemDragonShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.dragon_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDragonShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dragon_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDragonShield animatable) {
        return null;
    }
}
