package com.dungeon_additions.da.entity.model.flame_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityBareant;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameOrb;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelFlameOrb extends GeoModelExtended<EntityFlameOrb> {

    public ModelFlameOrb(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFlameOrb animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.volatile_orb.json");
    }
}
