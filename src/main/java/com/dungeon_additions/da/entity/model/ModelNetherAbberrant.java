package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelNetherAbberrant extends GeoModelExtended<EntityNetherAbberrant> {

    public ModelNetherAbberrant(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getModelLocation(EntityNetherAbberrant entityAbberrant) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/abberrant/abberrant.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNetherAbberrant entityAbberrant) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/abberrant.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityNetherAbberrant entityAbberrant) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.abberrant.json");
    }

    @Override
    public void setLivingAnimations(EntityNetherAbberrant entity, Integer uniqueID, AnimationEvent customPredicate) {
        if(!entity.isSpinAttack()) {
            IBone head = this.getAnimationProcessor().getBone("HeadJoint");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            super.setLivingAnimations(entity, uniqueID, customPredicate);
        }

        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
