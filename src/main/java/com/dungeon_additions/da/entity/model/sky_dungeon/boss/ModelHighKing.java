package com.dungeon_additions.da.entity.model.sky_dungeon.boss;

import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelHighKing extends GeoModelExtended<EntityHighKing> {

    public ModelHighKing(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHighKing animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.high_king.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHighKing object) {
        if(object.isBloodied()) {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king_bloody.png");
        } else {
            return new ResourceLocation(ModReference.MOD_ID, "textures/entity/high_king.png");
        }
    }

    @Override
    public void setLivingAnimations(EntityHighKing entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJ");
        if(!entity.isSummonBoss() && !entity.isDeathBoss() && !entity.isPhaseTransition()) {
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
