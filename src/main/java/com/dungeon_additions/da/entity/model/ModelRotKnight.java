package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.mini_blossom.EntityMiniBlossom;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelRotKnight extends AnimatedGeoModel<EntityRotKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityRotKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/rotknight/geo.rotknight.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRotKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/rotknight.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRotKnight animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.rotknight.json");
    }

    @Override
    public void setLivingAnimations(EntityRotKnight entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
