package com.dungeon_additions.da.tab;

import com.dungeon_additions.da.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;

public class DungeonAdditionsTab{
    public static CreativeTabs ALL = new CustomCreativeTab(CreativeTabs.getNextID(), "dungeon_additions", () -> ModItems.FLAME_HELMET);
}
