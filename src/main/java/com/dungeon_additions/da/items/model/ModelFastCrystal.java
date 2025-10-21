package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemFastCrystal;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelFastCrystal extends AnimatedGeoModel<ItemFastCrystal> {
    @Override
    public ResourceLocation getModelLocation(ItemFastCrystal object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.fast_crystal.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemFastCrystal object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/fast_crystal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemFastCrystal animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.fast_crystal.json");
    }
}
