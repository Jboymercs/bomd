package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityCursedSentinel;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityFriendlyCursedGolem;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelFriendlySentinel extends GeoModelExtended<EntityFriendlyCursedGolem> {
    public ModelFriendlySentinel(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFriendlyCursedGolem animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.cursed_golem.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFriendlyCursedGolem animatable) {
        if(animatable.isInAnimateState()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_inanimate.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_friendly.png");
    }

    @Override
    public void setLivingAnimations(EntityFriendlyCursedGolem entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!entity.isAwakeState() && !entity.isInAnimateState()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
