package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemFlameBlade;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelFlameBlade extends AnimatedGeoModel<ItemFlameBlade> {
    @Override
    public ResourceLocation getModelLocation(ItemFlameBlade object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.flame_blade.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemFlameBlade object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/flame_blade.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemFlameBlade animatable) {
        return null;
    }
}
