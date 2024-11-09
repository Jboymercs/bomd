package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGenericWave extends AnimatedGeoModel<EntityGenericWave> {
    @Override
    public ResourceLocation getModelLocation(EntityGenericWave emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/wave/wave.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGenericWave emtityGenericWave) {
        if(emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave1.png");
        }
        if(emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave2.png");
        }
        if(emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave3.png");
        }

        if(emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave4.png");
        }

        if(emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave5.png");
        }
        if(emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave2.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/wave/wave.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGenericWave emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wave.json");
    }
}
