package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.boss.EntityWreathKnight;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
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
                EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
                head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
                head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
            }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
