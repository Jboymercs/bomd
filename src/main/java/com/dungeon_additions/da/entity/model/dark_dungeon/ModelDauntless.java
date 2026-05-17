package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelDauntless extends GeoModelExtended<EntityDauntless> {


    public ModelDauntless(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDauntless animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dauntless.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDauntless animatable) {
        if(animatable.getSwordCharge() == 1) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_1.png");
        } else if (animatable.getSwordCharge() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_2.png");
        } else if (animatable.getSwordCharge() >= 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless_charged_3.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/dauntless/dauntless.png");
    }

    @Override
    public void setLivingAnimations(EntityDauntless entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone Body = this.getAnimationProcessor().getBone("BodyRot");
        IBone head = this.getAnimationProcessor().getBone("Torso");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        if(entity != null) {
            Body.setRotationX((float) Math.toRadians(((IPitch) entity).getPitch()));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
