package com.dungeon_additions.da.entity.model.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityAegyptianWarlord;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelAegyptianWarlord extends GeoModelExtended<EntityAegyptianWarlord> {

    public ModelAegyptianWarlord(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAegyptianWarlord animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.aegyptian_warlord.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAegyptianWarlord animatable) {
        if(animatable.isHasPhaseTransitioned()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_warlord_transition.png");
        } else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/aegyptia/aegyptian_warlord.png");
        }
    }

    @Override
    public void setLivingAnimations(EntityAegyptianWarlord entity, Integer uniqueID, AnimationEvent customPredicate) {
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
