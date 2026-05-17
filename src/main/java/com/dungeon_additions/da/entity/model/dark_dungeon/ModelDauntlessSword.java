package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessSword;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelDauntlessSword extends GeoModelExtended<EntityDauntlessSword> {
    public ModelDauntlessSword(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDauntlessSword animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dauntless_sword.json");
    }
}
