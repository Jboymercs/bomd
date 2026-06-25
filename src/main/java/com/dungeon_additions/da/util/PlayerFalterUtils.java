package com.dungeon_additions.da.util;

import com.dungeon_additions.da.capabilities.CapabilityPlayerFalter;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerFalterUtils {

    public static float getPlayerFalterProgress(EntityPlayer playerIn)
    {
        CapabilityPlayerFalter.IPlayerFalterCapability greed = getCapability(playerIn);
        if (greed == null) return 0.0F;

        return greed.getPlayerFalterProgress();
    }

    public static void setPlayerGreedProgress(EntityPlayer playerIn, float val) {
        CapabilityPlayerFalter.IPlayerFalterCapability greed = getCapability(playerIn);

        assert greed != null;
        greed.setPlayerFalterProgress(val);
    }


    public static float getPlayerFalterResistance(EntityPlayer playerIn)
    {
        CapabilityPlayerFalter.IPlayerFalterCapability greed = getCapability(playerIn);
        if (greed == null) return 0.0F;

        return greed.getPlayerFalterResistance();
    }

    public static void setPlayerFalterResistance(EntityPlayer playerIn, float val) {
        CapabilityPlayerFalter.IPlayerFalterCapability greed = getCapability(playerIn);

        assert greed != null;
        greed.setPlayerFalterResistance(val);
    }


    public static CapabilityPlayerFalter.IPlayerFalterCapability getCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null))
        { return playerIn.getCapability(CapabilityPlayerFalter.PLAYER_FALTER_CAP, null); }

        return null;
    }

}
