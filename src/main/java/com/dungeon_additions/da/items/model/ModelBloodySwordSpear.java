package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemBloodySwordSpear;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBloodySwordSpear extends AnimatedGeoModel<ItemBloodySwordSpear> {
    @Override
    public ResourceLocation getModelLocation(ItemBloodySwordSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.sword_spear.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemBloodySwordSpear object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/sword_spear_blood.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemBloodySwordSpear animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sword_spear.json");
    }
}
