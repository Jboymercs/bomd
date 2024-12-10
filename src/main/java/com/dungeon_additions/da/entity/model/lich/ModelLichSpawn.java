package com.dungeon_additions.da.entity.model.lich;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.night_lich.EntityLichSpawn;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelLichSpawn extends GeoModelExtended<EntityLichSpawn> {


    public ModelLichSpawn(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLichSpawn animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lich_spawn.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLichSpawn object) {
        if(object.ticksExisted <= 6) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spawn/lich_spawn_1.png");
        } else if ( object.ticksExisted <= 12 && object.ticksExisted > 7) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spawn/lich_spawn_2.png");
        } else if (object.ticksExisted >= 13 && object.ticksExisted < 20) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spawn/lich_spawn_3.png");
        } else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spawn/lich_spawn_4.png");
        }
    }
}
