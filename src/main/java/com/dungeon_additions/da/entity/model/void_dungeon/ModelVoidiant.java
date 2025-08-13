package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiant;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelVoidiant extends GeoModelExtended<EntityVoidiant> {

    public ModelVoidiant(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidiant animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.voidiant.json");
    }

    @Override
    public void setLivingAnimations(EntityVoidiant entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if(entity.isShootLazer()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
