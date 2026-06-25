package com.dungeon_additions.da.entity.model.dark_dungeon;

import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoidSpawn;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.EntityDauntlessAOE;
import com.dungeon_additions.da.entity.model.extended.GeoModelExtended;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;

public class ModelDarkVoidSpawn extends GeoModelExtended<EntityDarkVoidSpawn> {
    public ModelDarkVoidSpawn(ResourceLocation model, ResourceLocation textureDefault, String entityName) {
        super(model, textureDefault, entityName);
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDarkVoidSpawn animatable) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.dauntless_aoe.json");
    }

    @Override
    public void setLivingAnimations(EntityDarkVoidSpawn entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone Body = this.getAnimationProcessor().getBone("Base");
        float entityScale = entity.getTimeAlive() * 0.05F;
        Body.setScaleX(entityScale);
        Body.setScaleZ(entityScale);
        Body.setScaleY(1.5F);


    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }

}
