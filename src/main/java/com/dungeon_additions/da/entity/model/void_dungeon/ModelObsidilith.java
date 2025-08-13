package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelObsidilith extends GeoModelExtended<EntityObsidilith> {

    public ModelObsidilith(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityObsidilith animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.obsidilith.json");
    }
}
