package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBomb;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelVoidBomb extends GeoModelExtended<EntityVoidBomb> {
    public ModelVoidBomb(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidBomb animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.void_bomb.json");
    }
}
