package com.dungeon_additions.da.entity.model.sky_dungeon;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntityGargoyleLazer;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelDragonSpecial extends GeoModelExtended<EntityDragonSpecial> {
    public ModelDragonSpecial(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDragonSpecial animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dragon_special.json");
    }
}
