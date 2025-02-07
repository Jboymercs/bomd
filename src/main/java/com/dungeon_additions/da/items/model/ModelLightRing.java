package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.ItemMagicFireball;
import com.dungeon_additions.da.items.projectile.ItemLightRing;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelLightRing extends AnimatedGeoModel<ItemLightRing> {
    @Override
    public ResourceLocation getModelLocation(ItemLightRing object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.light_ring.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemLightRing object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/light_ring.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemLightRing animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.light_ring.json");
    }
}
