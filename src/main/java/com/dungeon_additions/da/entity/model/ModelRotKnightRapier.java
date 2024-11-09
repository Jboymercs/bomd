package com.dungeon_additions.da.entity.model;

import com.dungeon_additions.da.entity.flame_knight.EntityFlameKnight;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelRotKnightRapier extends GeoModelExtended<EntityRotKnightRapier> {

    public ModelRotKnightRapier(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityRotKnightRapier animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.rot_knight_r.json");
    }

    @Override
    public void setLivingAnimations(EntityRotKnightRapier entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
