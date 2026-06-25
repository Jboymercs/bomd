package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoid;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelDarkVoid extends GeoModelExtended<EntityDarkVoid> {
    public ModelDarkVoid(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDarkVoid animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dark_void.json");
    }
}
