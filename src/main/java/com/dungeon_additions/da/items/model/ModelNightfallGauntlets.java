package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemNightfallGauntlets;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelNightfallGauntlets extends AnimatedGeoModel<ItemNightfallGauntlets> {
    @Override
    public ResourceLocation getModelLocation(ItemNightfallGauntlets object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.nightfall_gauntlets.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemNightfallGauntlets object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_gauntlets.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemNightfallGauntlets animatable) {
        return null;
    }
}
