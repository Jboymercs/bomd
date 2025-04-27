package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemFlameBladeProj;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelFlameBladeProj extends AnimatedGeoModel<ItemFlameBladeProj> {
    @Override
    public ResourceLocation getModelLocation(ItemFlameBladeProj object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.proj_flame_blade.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemFlameBladeProj object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/flame_blade_proj.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemFlameBladeProj animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.flame_blade_proj.json");
    }
}
