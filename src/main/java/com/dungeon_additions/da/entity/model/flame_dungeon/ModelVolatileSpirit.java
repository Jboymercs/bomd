package com.dungeon_additions.da.entity.model.flame_dungeon;

import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.flame_knight.EntityVolatileSpirit;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelVolatileSpirit extends GeoModelExtended<EntityVolatileSpirit> {

    public ModelVolatileSpirit(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityVolatileSpirit animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.volatile_spirit.json");
    }

    @Override
    public void setLivingAnimations(EntityVolatileSpirit entity, Integer uniqueID, AnimationEvent customPredicate) {
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
