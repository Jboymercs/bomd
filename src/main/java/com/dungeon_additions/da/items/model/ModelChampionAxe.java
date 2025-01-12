package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.ItemMagicFireball;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelChampionAxe extends AnimatedGeoModel<ItemChampionAxe> {
    @Override
    public ResourceLocation getModelLocation(ItemChampionAxe object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.champion_axe.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemChampionAxe object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/champion_axe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemChampionAxe animatable) {
        return null;
    }
}
