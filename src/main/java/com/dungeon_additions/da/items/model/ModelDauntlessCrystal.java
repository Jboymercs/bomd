package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemDauntlessCrystal;
import com.dungeon_additions.da.items.projectile.ItemProjectileBloodMeteor;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDauntlessCrystal extends AnimatedGeoModel<ItemDauntlessCrystal> {
    @Override
    public ResourceLocation getModelLocation(ItemDauntlessCrystal object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.fast_crystal.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDauntlessCrystal object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dauntless_crystal.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDauntlessCrystal animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.fast_crystal.json");
    }
}
