package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.EntityGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelGreatWyrk extends GeoModelExtended<EntityGreatWyrk> {


    public ModelGreatWyrk(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGreatWyrk animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.great_wyrk.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGreatWyrk animatable) {
        if(animatable.hurtTime > 0 || animatable.isSummonBoss()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_wyrk_hurt.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/great_wyrk.png");
    }
}
