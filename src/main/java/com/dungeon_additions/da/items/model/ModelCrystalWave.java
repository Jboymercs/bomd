package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemYellowWave;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalWave extends AnimatedGeoModel<ItemYellowWave> {
    @Override
    public ResourceLocation getModelLocation(ItemYellowWave object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.yellow_wave.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemYellowWave object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/crystal_wave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemYellowWave animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.yellow_wave.json");
    }
}
