package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityCursedSentinel;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelCursedSentinel extends GeoModelExtended<EntityCursedSentinel> {

    public ModelCursedSentinel(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCursedSentinel animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.cursed_golem.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCursedSentinel animatable) {
        if(animatable.isInAnimateState()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem_inanimate.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/cursed_golem.png");
    }

    @Override
    public void setLivingAnimations(EntityCursedSentinel entity, Integer uniqueID, AnimationEvent customPredicate) {
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
