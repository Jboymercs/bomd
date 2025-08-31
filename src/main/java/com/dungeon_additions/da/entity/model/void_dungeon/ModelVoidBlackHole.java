package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.void_dungeon.EntityVoidBlackHole;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidBlackHole extends AnimatedGeoModel<EntityVoidBlackHole> {
    @Override
    public ResourceLocation getModelLocation(EntityVoidBlackHole object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/obsidilith/geo.void_hole.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityVoidBlackHole object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/void_hole.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidBlackHole animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.void_hole.json");
    }


}
