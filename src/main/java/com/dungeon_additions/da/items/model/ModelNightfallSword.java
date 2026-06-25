package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemNightfallSword;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.Collections;

public class ModelNightfallSword extends AnimatedGeoModel<ItemNightfallSword> {

    @Override
    public ResourceLocation getModelLocation(ItemNightfallSword object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.nightfall_sword.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemNightfallSword object) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/items/nightfall/nightfall_sword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemNightfallSword animatable) {
        return null;
    }
}
