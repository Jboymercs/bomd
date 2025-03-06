package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemStormTornado;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelStormTornado extends AnimatedGeoModel<ItemStormTornado> {

    @Override
    public ResourceLocation getModelLocation(ItemStormTornado object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.sky_tornado.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemStormTornado object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/sky_tornado.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemStormTornado animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sky_tornado.json");
    }
}
