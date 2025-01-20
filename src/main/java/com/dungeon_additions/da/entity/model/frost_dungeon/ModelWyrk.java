package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelWyrk extends GeoModelExtended<EntityWyrk> {

    public ModelWyrk(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityWyrk animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wyrk.json");
    }

}
