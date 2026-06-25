package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemDauntlessCrystal;
import com.dungeon_additions.da.items.projectile.ItemDauntlessFist;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDauntlessFist extends AnimatedGeoModel<ItemDauntlessFist> {
    @Override
    public ResourceLocation getModelLocation(ItemDauntlessFist object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.dauntless_fist.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDauntlessFist object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/dauntless_fist.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDauntlessFist animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dauntless_fist.json");
    }
}
