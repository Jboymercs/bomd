package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelFlameKnight extends GeoModelExtended<EntityFlameKnight> {

    public ModelFlameKnight(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getModelLocation(EntityFlameKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/flame_knight/geo.flame_knight.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFlameKnight object) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/flame_knight.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityFlameKnight animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.flame_knight.json");
    }

    @Override
    public void setLivingAnimations(EntityFlameKnight entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
            if(!entity.isSummonFlame() && !entity.isComboWaterFoul() && !entity.isBeginCombo()) {
                IBone head = this.getAnimationProcessor().getBone("HeadJ");
                IBone lowerTorso = this.getAnimationProcessor().getBone("LowerTorso");
                EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
              //  lowerTorso.setRotationX((float) MathHelper.clamp((Math.toRadians(extraData.headPitch/2)), Math.toRadians(-30), Math.toRadians(30) + Math.toRadians(10 * entity.limbSwing)));
             //   float bodyYRot = ((float) ((Math.toRadians((extraData.netHeadYaw)/ (entity.limbSwing >= 0.3 ? 1.5 : 4)) - Math.sin(entity.limbSwing / 2.07)/ (entity.limbSwing >= 0.3 ? 0.6 : 1) + Math.sin(entity.ticksExisted/60/2 * Math.PI * 2)/10 * MathHelper.clamp( 1 - entity.limbSwing, 0, 1)) + MathHelper.clamp(Math.toRadians(10)- entity.limbSwing * 10, 0, Math.toRadians(7))));
              //  lowerTorso.setRotationY((float)(Math.sin(entity.limbSwingAmount/2.07)/3 * entity.limbSwing + MathHelper.clamp(Math.toRadians(15)- entity.limbSwing * 10, 0, Math.toRadians(15))) - bodyYRot);
             //   double direction = (entity.motionX) - (entity.motionZ);
             //   lowerTorso.setRotationY((float) (Math.sin(entity.limbSwing/2.07)/3 * direction + MathHelper.clamp(Math.toRadians(45) - direction * 10, 0, Math.toRadians(45))));
            }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
