package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.shield.ItemDraugrShield;
import com.dungeon_additions.da.items.tools.ItemChampionAxe;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDraugrShield extends AnimatedGeoModel<ItemDraugrShield> {

    @Override
    public ResourceLocation getModelLocation(ItemDraugrShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.draugr_shield.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDraugrShield object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/draugr_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDraugrShield animatable) {
        return null;
    }
}
