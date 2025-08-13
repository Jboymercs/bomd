package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.shield.ItemObsidianShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelObsidianShield extends AnimatedGeoModel<ItemObsidianShield> {
    @Override
    public ResourceLocation getModelLocation(ItemObsidianShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.obsidian_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemObsidianShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/obsidian_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemObsidianShield animatable) {
        return null;
    }
}
