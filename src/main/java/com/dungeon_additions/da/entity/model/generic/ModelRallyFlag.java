package com.dungeon_additions.da.entity.model.generic;

import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.generic.EntityRallyFlag;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelRallyFlag extends GeoModelExtended<EntityRallyFlag> {
    public ModelRallyFlag(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRallyFlag animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.rally_flag.json");
    }
}
