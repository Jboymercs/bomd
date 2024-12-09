package com.dungeon_additions.da.entity.model.lich;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelLichStaff extends GeoModelExtended<EntityLichStaffAOE> {


    public ModelLichStaff(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLichStaffAOE animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lich_staff.json");
    }
}
