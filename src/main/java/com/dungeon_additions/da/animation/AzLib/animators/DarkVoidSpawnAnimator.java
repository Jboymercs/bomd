package com.dungeon_additions.da.animation.AzLib.animators;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.dark_void.EntityDarkVoidSpawn;
import mod.azure.azurelib.model.AzBakedModel;
import mod.azure.azurelib.model.AzBone;

public class DarkVoidSpawnAnimator extends DaEntityAnimator<EntityDarkVoidSpawn> {
    public DarkVoidSpawnAnimator(String animation_name) {
        super(animation_name, true, false);
    }

    @Override
    public void setCustomAnimations(EntityDarkVoidSpawn entity, float partialTicks) {
        super.setCustomAnimations(entity, partialTicks);
        AzBakedModel model = this.context().boneCache().getBakedModel();
        AzBone head = model.getBoneOrNull("Base");

        if (head == null) {
            return;
        }

        float entityScale = entity.getTimeAlive() * 0.05F;
        head.setScaleX(entityScale);
        head.setScaleZ(entityScale);
        head.setScaleY(1.5F);
    }

}
