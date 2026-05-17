package com.dungeon_additions.da.integration;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import com.dungeon_additions.da.config.CompatConfig;
import com.dungeon_additions.da.items.trinket.ItemTrinket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Many thanks to UnOriginal for a baubles compatibility. This source code is taken from his project, Beast Slayer
 */
public class BaublesIntegration {

    private static boolean isBaublesLoaded = Loader.isModLoaded("baubles") && CompatConfig.baubles_compat;;

    public static void init(){
        if(!isEnabled()) return;
    }


    public static boolean isEnabled(){
        return isBaublesLoaded;
    }



}
