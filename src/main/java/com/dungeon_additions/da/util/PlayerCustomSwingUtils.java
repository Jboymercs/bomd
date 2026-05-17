package com.dungeon_additions.da.util;

import com.dungeon_additions.da.capabilities.CapabilityPlayerSwing;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerCustomSwingUtils {

    public static int getPlayerSwingProgress(EntityPlayer playerIn)
    {
        CapabilityPlayerSwing.IPlayerSwingCapability greed = getCapability(playerIn);
        if (greed == null) return 0;

        return greed.getPlayerSwingProgress();
    }

    public static void setPlayerSwingProgress(EntityPlayer playerIn, int val) {
        CapabilityPlayerSwing.IPlayerSwingCapability greed = getCapability(playerIn);

        assert greed != null;
        greed.setPlayerSwingProgress(val);
    }

    public static boolean getPlayerSwingCancelled(EntityPlayer playerIn) {
        CapabilityPlayerSwing.IPlayerSwingCapability cancelled = getCapability(playerIn);

        if(cancelled == null) return false;
       return cancelled.isSwingEnabled();
    }

    public static void setPlayerSwingCancelled(EntityPlayer playerIn, boolean cancelled) {
        CapabilityPlayerSwing.IPlayerSwingCapability capability = getCapability(playerIn);
        assert capability != null;
        capability.setSwingEnabled(cancelled);
    }


    public static CapabilityPlayerSwing.IPlayerSwingCapability getCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null))
        { return playerIn.getCapability(CapabilityPlayerSwing.PLAYER_SWING_CAP, null); }

        return null;
    }
}
