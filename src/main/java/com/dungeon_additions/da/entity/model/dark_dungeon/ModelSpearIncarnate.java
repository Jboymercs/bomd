package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.boss.EntitySpearIncarnate;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelSpearIncarnate extends GeoModelExtended<EntitySpearIncarnate> {
    public ModelSpearIncarnate(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySpearIncarnate animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.spear_incarnate.json");
    }
}
