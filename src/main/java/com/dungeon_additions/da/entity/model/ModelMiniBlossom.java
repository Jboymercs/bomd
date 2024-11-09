package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelMiniBlossom extends AnimatedGeoModel<EntityMiniBlossom> {
    @Override
    public ResourceLocation getModelLocation(EntityMiniBlossom object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/blossom/geo.miniblossom.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMiniBlossom object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/miniblossom.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMiniBlossom animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.miniblossom.json");
    }

    @Override
    public void setLivingAnimations(EntityMiniBlossom entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("ModelCheckUse");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
