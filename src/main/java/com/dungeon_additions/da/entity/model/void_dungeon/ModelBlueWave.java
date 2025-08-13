package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.blossom.EntityGenericWave;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBlueWave extends AnimatedGeoModel<EntityBlueWave> {
    @Override
    public ResourceLocation getModelLocation(EntityBlueWave emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/wave/wave.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBlueWave emtityGenericWave) {
        //RED
        if(emtityGenericWave.isRedStyle()) {
            if (emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red1.png");
            }
            if (emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red2.png");
            }
            if (emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red3.png");
            }

            if (emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red4.png");
            }

            if (emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red5.png");
            }
            if (emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red2.png");
            }
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/red.png");
            //FLAME
        } else if (emtityGenericWave.isFlameStyle()) {
            if (emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame1.png");
            }
            if (emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame2.png");
            }
            if (emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame3.png");
            }

            if (emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame4.png");
            }

            if (emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame5.png");
            }
            if (emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame2.png");
            }
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/flame.png");
            //PURPLE
        }  else if (emtityGenericWave.isPurpleStyle()) {
            if (emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple1.png");
            }
            if (emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple2.png");
            }
            if (emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple3.png");
            }

            if (emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple4.png");
            }

            if (emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple5.png");
            }
            if (emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/purple2.png");
            }
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/empty.png");
            //BLUE
        } else {
            if (emtityGenericWave.ticksExisted > 1 && emtityGenericWave.ticksExisted < 4) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave1.png");
            }
            if (emtityGenericWave.ticksExisted > 4 && emtityGenericWave.ticksExisted < 8) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave2.png");
            }
            if (emtityGenericWave.ticksExisted > 8 && emtityGenericWave.ticksExisted < 12) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave3.png");
            }

            if (emtityGenericWave.ticksExisted > 12 && emtityGenericWave.ticksExisted < 16) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave4.png");
            }

            if (emtityGenericWave.ticksExisted > 16 && emtityGenericWave.ticksExisted < 20) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave5.png");
            }
            if (emtityGenericWave.ticksExisted > 20 && emtityGenericWave.ticksExisted < 25) {
                return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave2.png");
            }
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/volley/wave.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBlueWave emtityGenericWave) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.wave.json");
    }
}
