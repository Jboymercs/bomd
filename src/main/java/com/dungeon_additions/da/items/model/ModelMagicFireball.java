package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.AnimatedSporeItem;
import com.dungeon_additions.da.items.ItemMagicFireball;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMagicFireball extends AnimatedGeoModel<ItemMagicFireball> {


    @Override
    public ResourceLocation getModelLocation(ItemMagicFireball object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/magic_fireball.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemMagicFireball object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/magic_fireball.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemMagicFireball animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.magic_fireball.json");
    }
}
