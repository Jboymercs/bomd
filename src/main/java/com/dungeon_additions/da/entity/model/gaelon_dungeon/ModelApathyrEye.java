package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.EntityApathyrEye;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelApathyrEye extends GeoModelExtended<EntityApathyrEye> {


    public ModelApathyrEye(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityApathyrEye animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.apathyr_eye.json");
    }

    @Override
    public void setLivingAnimations(EntityApathyrEye entity, Integer uniqueID, AnimationEvent customPredicate) {
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
