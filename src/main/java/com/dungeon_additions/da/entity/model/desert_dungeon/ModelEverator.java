package com.dungeon_additions.da.entity.model.desert_dungeon;

import com.dungeon_additions.da.entity.desert_dungeon.aegyptia.EntityAegyptia;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.EntityEverator;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEverator extends GeoModelExtended<EntityEverator> {


    public ModelEverator(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEverator animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.everator.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEverator animatable) {
        if(animatable.isAwake() || animatable.isInactiveMode()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/everator_inactive.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/everator.png");
    }

    @Override
    public void setLivingAnimations(EntityEverator entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone head = this.getAnimationProcessor().getBone("Head");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if(!entity.isAwake() && !entity.isInactiveMode()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
