package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.EntityTestModel;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelTestModel extends GeoModelExtended<EntityTestModel> {
    public ModelTestModel(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityTestModel animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.test_model.json");
    }
}
