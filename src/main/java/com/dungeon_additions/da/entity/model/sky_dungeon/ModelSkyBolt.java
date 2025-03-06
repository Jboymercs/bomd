package com.dungeon_additions.da.entity.model.sky_dungeon;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelSkyBolt extends GeoModelExtended<EntitySkyBolt> {

    public ModelSkyBolt(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySkyBolt animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySkyBolt animatable) {
        if(animatable.ticksExisted < 30 && animatable.ticksExisted > 20) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bolt/lightning_bolt_" + (animatable.ticksExisted) + ".png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bolt/lightning_bolt.png");
    }
}
