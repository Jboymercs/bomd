package com.dungeon_additions.da.items.model;

import com.dungeon_additions.da.items.projectile.ItemProjectileGhost;
import com.dungeon_additions.da.items.projectile.ItemVoidclysmBolt;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGhostProjectile extends AnimatedGeoModel<ItemProjectileGhost> {
    @Override
    public ResourceLocation getModelLocation(ItemProjectileGhost object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.voidclysm_bolt.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemProjectileGhost object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/items/ghost_bolt.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemProjectileGhost animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.voidclysm_bolt.json");
    }
}
