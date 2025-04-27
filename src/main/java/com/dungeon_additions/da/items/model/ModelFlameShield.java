package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDragonShield;
import com.dungeon_additions.da.items.shield.ItemFlameShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelFlameShield extends AnimatedGeoModel<ItemFlameShield> {
    @Override
    public ResourceLocation getModelLocation(ItemFlameShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.flame_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemFlameShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/flame_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemFlameShield animatable) {
        return null;
    }
}
