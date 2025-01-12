package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelDraugrRanger extends GeoModelExtended<EntityDraugrRanger> {
    public ModelDraugrRanger(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDraugrRanger animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.draugr_ranger.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDraugrRanger animatable) {
        if(animatable.getSkin() == 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_ranger_4.png");
        } else if (animatable.getSkin() == 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_ranger_3.png");
        } else if (animatable.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_ranger_2.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_ranger.png");
    }

    @Override
    public void setLivingAnimations(EntityDraugrRanger entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        IBone LArm = this.getAnimationProcessor().getBone("LArm");
        IBone RArm = this.getAnimationProcessor().getBone("RArm");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if(entity.isRangedAttack()) {
            RArm.setRotationX(((float)Math.PI / 2F) + head.getRotationX() + 0.1F);
            RArm.setRotationY(0.3F + head.getRotationY());
            LArm.setRotationX(1.5F + head.getRotationX());
            LArm.setRotationY(-0.6F + head.getRotationY());
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
