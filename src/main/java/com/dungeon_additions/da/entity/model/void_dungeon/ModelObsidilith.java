package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;

public class ModelObsidilith extends GeoModelExtended<EntityObsidilith> {

    public ModelObsidilith(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityObsidilith obsidilith) {
        if(obsidilith.isPurpleAttack()) {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith/obsidilith_purple.png");
        } else if (obsidilith.isRedAttack()) {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith/obsidilith_red.png");
        } else if (obsidilith.isFlameAttack()) {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith/obsidilith_orange.png");
        } else if (obsidilith.isBlueAttack()) {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith/obsidilith_blue.png");
        } else if (obsidilith.isYellowAttack()) {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith/obsidilith_yellow.png");
        } else {
            return new ResourceLocation(ModReference.MOD_ID,"textures/entity/obsidilith.png");
        }
    }


    @Override
    public ResourceLocation getAnimationFileLocation(EntityObsidilith animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.obsidilith.json");
    }
}
