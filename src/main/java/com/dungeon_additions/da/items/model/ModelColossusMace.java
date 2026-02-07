package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.items.tools.ItemColossusMace;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelColossusMace extends AnimatedGeoModel<ItemColossusMace> {
    @Override
    public ResourceLocation getModelLocation(ItemColossusMace object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.colossus_mace.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemColossusMace object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/colossus_mace.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemColossusMace animatable) {
        return null;
    }
}
