package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelRotSpike extends GeoModelExtended<EntityRotSpike> {


    public ModelRotSpike(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRotSpike animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.rot_spike.json");
    }
}
