package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrkFoot;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelWyrkFoot extends GeoModelExtended<EntityWyrkFoot> {
    public ModelWyrkFoot(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityWyrkFoot animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wyrk_foot.json");
    }
}
