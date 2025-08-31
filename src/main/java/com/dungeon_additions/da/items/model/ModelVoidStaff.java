package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemStormTornado;
import com.dungeon_additions.da.items.tools.ItemVoidStaff;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidStaff extends AnimatedGeoModel<ItemVoidStaff> {
    @Override
    public ResourceLocation getModelLocation(ItemVoidStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.void_staff.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemVoidStaff object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/void_staff.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemVoidStaff animatable) {
        return null;
    }
}
