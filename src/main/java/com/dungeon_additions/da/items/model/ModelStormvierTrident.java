package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemTridentProjectile;
import com.dungeon_additions.da.items.tools.ItemStormvierTrident;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelStormvierTrident extends AnimatedGeoModel<ItemStormvierTrident> {
    @Override
    public ResourceLocation getModelLocation(ItemStormvierTrident object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.stormvier_trident.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemStormvierTrident object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/stormvier_trident.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemStormvierTrident animatable) {
        return null;
    }
}
