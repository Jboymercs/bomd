package com.dungeon_additions.da.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class AnimationCapabilityHelper {

    public static boolean isPlayerParryItemAnimation(EntityPlayer playerIn)
    {
        CapabilityItemAnimations.ICapabilityItemAnimations capAnim = getAnimationCapability(playerIn);
        if (capAnim == null) return false;

        return capAnim.getParryEndTime() > playerIn.ticksExisted;
    }

    public static float getPlayerParryAnimProgress(EntityPlayer playerIn, float particalTick)
    {
        CapabilityItemAnimations.ICapabilityItemAnimations capAnim = getAnimationCapability(playerIn);
        if (capAnim == null) return 0;

        float duration = capAnim.getParryEndTime() - capAnim.getParryStartTime();
        float elapsed = (playerIn.ticksExisted + particalTick) - capAnim.getParryStartTime();

        return MathHelper.clamp(elapsed / duration, 0F, 1F);
    }



    public static boolean isPlayerCustomSwingAnimating(EntityPlayer playerIn)
    {
        CapabilityItemAnimations.ICapabilityItemAnimations capAnim = getAnimationCapability(playerIn);
        if (capAnim == null) return false;

        return capAnim.getCustomSwingEndTime() > playerIn.ticksExisted;
    }

    public static float getPlayerCustomSwingAnimProgress(EntityPlayer playerIn, float particalTick)
    {
        CapabilityItemAnimations.ICapabilityItemAnimations capAnim = getAnimationCapability(playerIn);
        if (capAnim == null) return 0;

        float duration = capAnim.getCustomSwingEndTime() - capAnim.getCustomSwingStartTime();
        float elapsed = (playerIn.ticksExisted + particalTick) - capAnim.getCustomSwingStartTime();

        return MathHelper.clamp(elapsed / duration, 0F, 1F);
    }

    public static CapabilityItemAnimations.ICapabilityItemAnimations getAnimationCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityItemAnimations.ANIM_CAP, null))
        { return playerIn.getCapability(CapabilityItemAnimations.ANIM_CAP, null); }

        return null;
    }

}
