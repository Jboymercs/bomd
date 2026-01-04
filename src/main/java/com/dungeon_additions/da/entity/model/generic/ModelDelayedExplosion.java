package com.dungeon_additions.da.entity.model.generic;

import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelDelayedExplosion extends GeoModelExtended<EntityDelayedExplosion> {
    public ModelDelayedExplosion(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDelayedExplosion animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.delayed_explosion.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDelayedExplosion animatable) {
        if(animatable.isOrangeStyle()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_orange.png");
        } else if(animatable.isPurpleStyle()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_purple.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/generic/delayed_explosion_blue.png");
    }

}
