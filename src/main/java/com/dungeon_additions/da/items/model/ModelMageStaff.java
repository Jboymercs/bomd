package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.tools.ItemKingClaw;
import com.dungeon_additions.da.items.tools.ItemMageStaff;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMageStaff extends AnimatedGeoModel<ItemMageStaff> {
    @Override
    public ResourceLocation getModelLocation(ItemMageStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.mage_staff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemMageStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/mage_staff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemMageStaff animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.mage_staff.json");
    }
}
