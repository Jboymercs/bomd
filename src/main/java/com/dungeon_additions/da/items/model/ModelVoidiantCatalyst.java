package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.gun.ItemVoidiantCatalyst;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidiantCatalyst extends AnimatedGeoModel<ItemVoidiantCatalyst> {
    @Override
    public ResourceLocation getModelLocation(ItemVoidiantCatalyst object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.voidiant_catalyst.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemVoidiantCatalyst object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/voidiant_catalyst.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemVoidiantCatalyst animatable) {
        return null;
    }
}
