package com.dungeon_additions.da.entity.model.sky_dungeon;

import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityBloodPile;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBloodPile extends AnimatedGeoModel<EntityBloodPile> {
    @Override
    public ResourceLocation getModelLocation(EntityBloodPile object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/wave/geo.blood_pile.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBloodPile object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/blood_pile.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBloodPile animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.blood_pile.json");
    }
}
