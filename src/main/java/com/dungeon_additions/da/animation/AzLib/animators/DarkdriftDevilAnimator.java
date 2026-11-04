package com.dungeon_additions.da.animation.AzLib.animators;

import com.dungeon_additions.da.animation.AzLib.DaEntityAnimator;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityDarkdriftDevil;
import com.dungeon_additions.da.util.ModUtils;
import mod.azure.azurelib.model.AzBakedModel;
import mod.azure.azurelib.model.AzBone;
import mod.azure.azurelib.model.AzBoneSnapshot;
import net.minecraft.util.math.MathHelper;

public class DarkdriftDevilAnimator extends DaEntityAnimator<EntityDarkdriftDevil> {

    public DarkdriftDevilAnimator(String animation_name) {
        super(animation_name, false, false);
    }

    @Override
    public void setCustomAnimations(EntityDarkdriftDevil entity, float partialTicks) {
        super.setCustomAnimations(entity, partialTicks);
        AzBakedModel model = this.context().boneCache().getBakedModel();
        AzBone head = model.getBoneOrNull("HeadJ");

        if (head == null) {
            return;
        }

        if(!entity.isCastDespair() && !entity.isDeathState()) {
            // Where the entity is looking, relative to its body, in degrees.
            float bodyYaw = ModUtils.rotLerp(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
            float headYaw = MathHelper.wrapDegrees(ModUtils.rotLerp(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead) - bodyYaw);
            float headPitch = ModUtils.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
            AzBoneSnapshot initial = head.getInitialAzSnapshot();
            head.setRotX(initial.getRotX() + headPitch * ((float) Math.PI / 180F));
            head.setRotY(initial.getRotY() + headYaw * ((float) Math.PI / 180F));
        }
    }
}
