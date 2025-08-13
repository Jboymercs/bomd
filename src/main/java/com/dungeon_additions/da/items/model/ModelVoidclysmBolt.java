package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemFlameBladeProj;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidclysmBolt extends AnimatedGeoModel<ItemVoidclysmBolt> {
    @Override
    public ResourceLocation getModelLocation(ItemVoidclysmBolt object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.voidclysm_bolt.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemVoidclysmBolt object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/voidclysm_bolt.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemVoidclysmBolt animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.voidclysm_bolt.json");
    }
}
