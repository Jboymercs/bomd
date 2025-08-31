package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemDesertStorm;
import com.dungeon_additions.da.items.projectile.ItemFlameBladeProj;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDesertStorm extends AnimatedGeoModel<ItemDesertStorm> {
    @Override
    public ResourceLocation getModelLocation(ItemDesertStorm object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.desert_storm.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDesertStorm object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/desert_storm.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDesertStorm animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.desert_storm.json");
    }
}
