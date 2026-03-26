package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemSorceryDagger;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSorceryDagger  extends AnimatedGeoModel<ItemSorceryDagger> {
    @Override
    public ResourceLocation getModelLocation(ItemSorceryDagger object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.sorcery_dagger.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemSorceryDagger object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/sorcery_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemSorceryDagger animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sorcery_dagger.json");
    }
}
