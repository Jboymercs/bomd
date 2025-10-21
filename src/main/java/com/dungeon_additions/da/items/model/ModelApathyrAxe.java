package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemApathyrAxe;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelApathyrAxe extends AnimatedGeoModel<ItemApathyrAxe> {

    @Override
    public ResourceLocation getModelLocation(ItemApathyrAxe object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.apathyr_axe.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemApathyrAxe object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/apathyr_axe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemApathyrAxe animatable) {
        return null;
    }
}
