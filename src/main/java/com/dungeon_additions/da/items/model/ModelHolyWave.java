package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemHolyWave;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelHolyWave extends AnimatedGeoModel<ItemHolyWave> {
    @Override
    public ResourceLocation getModelLocation(ItemHolyWave object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.holy_wave.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemHolyWave object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/holy_wave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemHolyWave animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.holy_wave.json");
    }
}
