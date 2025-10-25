package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.gun.ItemGolemCannon;
import com.dungeon_additions.da.items.shield.ItemFlameShield;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGolemCannon extends AnimatedGeoModel<ItemGolemCannon> {
    @Override
    public ResourceLocation getModelLocation(ItemGolemCannon object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.golem_cannon.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemGolemCannon object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/golem_cannon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemGolemCannon animatable) {
        return null;
    }
}
