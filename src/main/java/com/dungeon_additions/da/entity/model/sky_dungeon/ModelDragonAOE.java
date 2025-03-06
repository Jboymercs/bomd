package com.dungeon_additions.da.entity.model.sky_dungeon;

import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDragonAOE extends AnimatedGeoModel<EntityDragonAOE> {
    @Override
    public ResourceLocation getModelLocation(EntityDragonAOE object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/sky/geo.dragon_aoe.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDragonAOE object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/dragon_aoe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDragonAOE animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dragon_aoe.json");
    }
}
