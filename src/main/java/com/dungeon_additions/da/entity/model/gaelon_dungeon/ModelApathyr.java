package com.dungeon_additions.da.entity.model.gaelon_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyr;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityReAnimate;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelApathyr extends GeoModelExtended<EntityApathyr> {

    public ModelApathyr(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityApathyr animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.apathyr.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityApathyr animatable) {
        if(animatable.isIdleState()) {
           return new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_idle.png");
        } else if(animatable.getHealth() / animatable.getMaxHealth() < 0.5) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr_broken.png");
        }
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/apathyr.png");
    }

    @Override
    public void setLivingAnimations(EntityApathyr entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!entity.isIdleAwake() && !entity.isIdleState() && !entity.isDeathState()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
