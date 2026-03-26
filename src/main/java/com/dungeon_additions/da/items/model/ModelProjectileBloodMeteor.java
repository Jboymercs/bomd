package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemFastCrystal;
import com.dungeon_additions.da.items.projectile.ItemProjectileBloodMeteor;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelProjectileBloodMeteor extends AnimatedGeoModel<ItemProjectileBloodMeteor> {
    @Override
    public ResourceLocation getModelLocation(ItemProjectileBloodMeteor object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.fast_crystal.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemProjectileBloodMeteor object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/blood_fast_crystal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemProjectileBloodMeteor animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.fast_crystal.json");
    }
}
