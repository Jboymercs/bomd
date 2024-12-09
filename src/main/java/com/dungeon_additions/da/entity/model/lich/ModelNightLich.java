package com.dungeon_additions.da.entity.model.lich;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelNightLich extends GeoModelExtended<EntityNightLich> {


    public ModelNightLich(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityNightLich animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.night_lich.json");
    }

    @Override
    public void setLivingAnimations(EntityNightLich entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
            IBone head = this.getAnimationProcessor().getBone("HeadJ");
            IBone Body = this.getAnimationProcessor().getBone("Ground");
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
