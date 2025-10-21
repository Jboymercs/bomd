package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.gaelon_dungeon.friendly.EntityFriendlyCursedRevenant;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelFriendlyCursedRevenant extends GeoModelExtended<EntityFriendlyCursedRevenant> {

    public ModelFriendlyCursedRevenant(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFriendlyCursedRevenant animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.reanimate.json");
    }

    @Override
    public void setLivingAnimations(EntityFriendlyCursedRevenant entity, Integer uniqueID, AnimationEvent customPredicate) {
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
