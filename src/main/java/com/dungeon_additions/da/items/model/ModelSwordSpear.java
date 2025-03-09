package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemStormTornado;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSwordSpear extends AnimatedGeoModel<ItemSwordSpear> {
    @Override
    public ResourceLocation getModelLocation(ItemSwordSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.sword_spear.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemSwordSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/sword_spear.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemSwordSpear animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sword_spear.json");
    }
}
