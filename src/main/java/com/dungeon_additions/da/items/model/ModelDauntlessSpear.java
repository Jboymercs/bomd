package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemDauntlessSpear;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDauntlessSpear extends AnimatedGeoModel<ItemDauntlessSpear> {


    @Override
    public ResourceLocation getModelLocation(ItemDauntlessSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.d_spear.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDauntlessSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dauntless_spear.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDauntlessSpear animatable) {
        return null;
    }
}
