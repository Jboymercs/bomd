package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDemonRitual;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import net.minecraft.util.ResourceLocation;

public class ModelDemonRitual extends GeoModelExtended<EntityDemonRitual> {
    public ModelDemonRitual(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDemonRitual animatable) {
        return null;
    }
}
