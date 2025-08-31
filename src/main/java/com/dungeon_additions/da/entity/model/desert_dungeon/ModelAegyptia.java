package com.dungeon_additions.da.entity.model.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelAegyptia extends GeoModelExtended<EntityAegyptia> {

    public ModelAegyptia(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAegyptia animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.aegyptia.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAegyptia animatable) {
        if(animatable.getSkin() == 1) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia.png");
        } else if (animatable.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia_2.png");
        } else if (animatable.getSkin() == 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia_3.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptia.png");
    }

    @Override
    public void setLivingAnimations(EntityAegyptia entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJoint");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
