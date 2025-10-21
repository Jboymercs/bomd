package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelApathyrSpear extends GeoModelExtended<EntityApathyrSpear> {
    public ModelApathyrSpear(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityApathyrSpear animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.apathyr_spear.json");
    }
}
