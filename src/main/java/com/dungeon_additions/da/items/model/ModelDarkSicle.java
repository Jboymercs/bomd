package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDarkShield;
import com.dungeon_additions.da.items.tools.ItemDarkSicle;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDarkSicle extends AnimatedGeoModel<ItemDarkSicle> {
    @Override
    public ResourceLocation getModelLocation(ItemDarkSicle object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.dark_sicle.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDarkSicle object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dark_sicle.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDarkSicle animatable) {
        return null;
    }
}
