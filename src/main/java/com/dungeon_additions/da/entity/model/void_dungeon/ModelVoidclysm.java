package com.dungeon_additions.da.entity.model.void_dungeon;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.void_dungeon.EntityObsidilith;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelVoidclysm extends GeoModelExtended<EntityVoidiclysm> {

    public ModelVoidclysm(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVoidiclysm animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.voidclysm.json");
    }

    @Override
    public void setLivingAnimations(EntityVoidiclysm entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        IBone head2 = this.getAnimationProcessor().getBone("HeadJ2");
        IBone head3 = this.getAnimationProcessor().getBone("HeadJ3");
        IBone Body = this.getAnimationProcessor().getBone("TorsoTwo");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head2.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 140F));
        head2.setRotationX(extraData.headPitch * ((float) Math.PI / 140F));
        head3.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 140F));
        head3.setRotationX(extraData.headPitch * ((float) Math.PI / 140F));
        if(entity != null) {
            Body.setRotationX((float) Math.toRadians(((IPitch) entity).getPitch()));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
