package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelWreathKnight extends AnimatedGeoModel<EntityWreathKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityWreathKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/geo.jboss.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWreathKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/jboss.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityWreathKnight animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.jboss.json");
    }

    @Override
    public void setLivingAnimations(EntityWreathKnight entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        IBone hips = this.getAnimationProcessor().getBone("Belt");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

        hips.setRotationY((float) entity.setWaistLook);

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
