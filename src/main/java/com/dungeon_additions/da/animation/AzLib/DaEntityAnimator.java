package com.dungeon_additions.da.animation.AzLib;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import mod.azure.azurelib.animation.controller.AzAnimationController;
import mod.azure.azurelib.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.animation.impl.AzEntityAnimator;
import mod.azure.azurelib.model.AzBakedModel;
import mod.azure.azurelib.model.AzBone;
import mod.azure.azurelib.model.AzBoneSnapshot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base class that should cover most entities for the animator.
 */
public class DaEntityAnimator <T extends EntityAbstractBase> extends AzEntityAnimator<T>{

    private String animation_name;
    private boolean isSpell;
    private boolean hasHead;
    private String headBoneName = null;

    public DaEntityAnimator(String animation_name, boolean isSpell, boolean hasHead) {
        this.animation_name = animation_name;
        this.isSpell = isSpell;
        this.hasHead = hasHead;
    }

    public DaEntityAnimator(String animation_name, boolean isSpell, boolean hasHead, @Nullable String headBoneName) {
        this.animation_name = animation_name;
        this.isSpell = isSpell;
        this.hasHead = hasHead;
        this.headBoneName = headBoneName;
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<T> animationControllerContainer) {
        if(this.isSpell) {
            animationControllerContainer.add(
                    AzAnimationController.builder(this, DaReferenceStrings.IDLE_FULL_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.STATE_CTR_ONE).build()
            );
        } else {
            animationControllerContainer.add(
                    AzAnimationController.builder(this, DaReferenceStrings.IDLE_UPPER_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.IDLE_LOWER_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.WALK_UPPER_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.WALK_LOWER_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.ATTACK_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.COMBO_CTR).setTransitionLength(8).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.STATE_CTR_ONE).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.IDLE_FULL_CTR).build(),
                    AzAnimationController.builder(this, DaReferenceStrings.WALK_FULL_CTR).build()
            );
        }
    }

    @Nonnull
    @Override
    public ResourceLocation getAnimationLocation(EntityAbstractBase entityAbstractBase) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation." + animation_name + ".json");
    }

    @Override
    public void setCustomAnimations(T entity, float partialTicks) {
        super.setCustomAnimations(entity, partialTicks);
        if (hasHead) {
            AzBakedModel model = this.context().boneCache().getBakedModel();
            AzBone head = model.getBoneOrNull(headBoneName != null ? headBoneName : "Head");

            if (head == null) {
                return;
            }

            // Where the entity is looking, relative to its body, in degrees.
            float bodyYaw = ModUtils.rotLerp(partialTicks, entity.prevRenderYawOffset, entity.renderYawOffset);
            float headYaw = MathHelper.wrapDegrees(ModUtils.rotLerp(partialTicks, entity.prevRotationYawHead, entity.rotationYawHead) - bodyYaw);
            float headPitch = ModUtils.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
            //for the default class, if the entity has it's look locked it will remain facing that direction.
            if (!entity.lockLook) {
                AzBoneSnapshot initial = head.getInitialAzSnapshot();
                head.setRotX(initial.getRotX() + headPitch * ((float) Math.PI / 180F));
                head.setRotY(initial.getRotY() + headYaw * ((float) Math.PI / 180F));
            }
        }
    }
}
