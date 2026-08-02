package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemTridentProjectile;
import com.dungeon_additions.da.items.tools.ItemSwordSpear;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelTridentProjectile extends AnimatedGeoModel<ItemTridentProjectile> {
    @Override
    public ResourceLocation getModelLocation(ItemTridentProjectile object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.stormvier_trident.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemTridentProjectile object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/stormvier_trident.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemTridentProjectile animatable) {
        return null;
    }
}
