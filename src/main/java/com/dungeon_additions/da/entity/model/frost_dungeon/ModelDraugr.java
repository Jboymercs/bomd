package com.dungeon_additions.da.entity.model.frost_dungeon;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelDraugr extends GeoModelExtended<EntityDraugr> {


    public ModelDraugr(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDraugr animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.draugr.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDraugr animatable) {
        if(animatable.getSkin() == 4) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_4.png");
        } else if (animatable.getSkin() == 3) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_3.png");
        } else if (animatable.getSkin() == 2) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr_2.png");
        }
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/draugr.png");
    }

    @Override
    public void setLivingAnimations(EntityDraugr entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
