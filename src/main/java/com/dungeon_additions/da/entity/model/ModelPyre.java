package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelPyre extends GeoModelExtended<EntityPyre> {
    public ModelPyre(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityPyre animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.pyre.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPyre object) {
        if(object.isFightMode() && !object.isCracked) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre_lit.png");
        } else if (object.isCracked) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre_cracked.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/pyre.png");
    }


}
