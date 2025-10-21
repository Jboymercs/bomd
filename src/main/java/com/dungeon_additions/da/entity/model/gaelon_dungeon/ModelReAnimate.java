package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelReAnimate extends GeoModelExtended<EntityReAnimate> {


    public ModelReAnimate(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityReAnimate animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.reanimate.json");
    }

    @Override
    public void setLivingAnimations(EntityReAnimate entity, Integer uniqueID, AnimationEvent customPredicate) {
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
