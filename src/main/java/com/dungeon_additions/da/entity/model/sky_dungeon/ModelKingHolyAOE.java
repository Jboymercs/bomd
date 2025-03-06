package com.dungeon_additions.da.entity.model.sky_dungeon;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonSpecial;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyAOE;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelKingHolyAOE extends GeoModelExtended<EntityKingHolyAOE> {
    public ModelKingHolyAOE(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityKingHolyAOE animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.king_holy_aoe.json");
    }
}
