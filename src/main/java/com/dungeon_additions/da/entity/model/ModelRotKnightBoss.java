package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightBoss;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelRotKnightBoss extends GeoModelExtended<EntityRotKnightBoss> {
    public ModelRotKnightBoss(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRotKnightBoss animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.fallen.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRotKnightBoss object) {
        if(!object.isHideArms()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/fallen_eyes.png");
        } else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/fallen.png");
        }
    }

    @Override
    public void setLivingAnimations(EntityRotKnightBoss entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone head = this.getAnimationProcessor().getBone("Head");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}