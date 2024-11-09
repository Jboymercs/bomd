package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.AnimatedSporeItem;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSpore extends AnimatedGeoModel<AnimatedSporeItem> {
    @Override
    public ResourceLocation getModelLocation(AnimatedSporeItem animatedSporeItem) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/spore.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatedSporeItem animatedSporeItem) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/spore.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AnimatedSporeItem animatedSporeItem) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.spore.json");
    }
}
