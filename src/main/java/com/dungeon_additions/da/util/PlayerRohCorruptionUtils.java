package com.dungeon_additions.da.util;

import com.dungeon_additions.da.capabilities.CapabilityRohsCorruption;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerRohCorruptionUtils {

    public static int getRohsCorruption(EntityPlayer playerIn)
    {
        CapabilityRohsCorruption.IRohsCorruptionCapability greed = getCapability(playerIn);
        if (greed == null) return 0;

        return greed.getRohsCorruption();
    }

    public static void setRohsCorruption(EntityPlayer playerIn, int val) {
        CapabilityRohsCorruption.IRohsCorruptionCapability greed = getCapability(playerIn);

        assert greed != null;
        greed.setRohsCorruption(val);
    }

    public static CapabilityRohsCorruption.IRohsCorruptionCapability getCapability(EntityPlayer playerIn)
    {
        if (playerIn.hasCapability(CapabilityRohsCorruption.PLAYER_ROH_CORRUPTION_CAP, null))
        { return playerIn.getCapability(CapabilityRohsCorruption.PLAYER_ROH_CORRUPTION_CAP, null); }

        return null;
    }

}
