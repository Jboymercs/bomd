package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelIcSpike extends GeoModelExtended<EntityIcicleSpike> {

    public ModelIcSpike(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityIcicleSpike animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.ice_spike.json");
    }


}
