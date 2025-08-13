package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidclysmSpike;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelVoidclysmSpike extends AnimatedGeoModel<EntityVoidclysmSpike> {

    @Override
    public ResourceLocation getModelLocation(EntityVoidclysmSpike entityVoidSpike) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/spike/spike.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityVoidclysmSpike entityVoidSpike) {
        if(entityVoidSpike.ticksExisted > 1 && entityVoidSpike.ticksExisted < 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike.png");
        }
        if(entityVoidSpike.ticksExisted > 4 && entityVoidSpike.ticksExisted < 8) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike2.png");
        }
        if(entityVoidSpike.ticksExisted > 8 && entityVoidSpike.ticksExisted < 12) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike3.png");
        }

        if(entityVoidSpike.ticksExisted > 12 && entityVoidSpike.ticksExisted < 16) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike4.png");
        }

        if(entityVoidSpike.ticksExisted > 16 && entityVoidSpike.ticksExisted < 20) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike5.png");
        }
        if(entityVoidSpike.ticksExisted > 20 && entityVoidSpike.ticksExisted < 25) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike6.png");
        }
        if(entityVoidSpike.ticksExisted > 25 && entityVoidSpike.ticksExisted < 28) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike7.png");
        }
        if(entityVoidSpike.ticksExisted > 28 && entityVoidSpike.ticksExisted < 32) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/voidspike8.png");
        }
        if(entityVoidSpike.ticksExisted > 32) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/spike9.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/spike/spike9.png");

    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidclysmSpike entityVoidSpike) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.spike.json");
    }
}
