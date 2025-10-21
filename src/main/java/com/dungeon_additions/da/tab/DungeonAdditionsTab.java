package com.dungeon_additions.da.tab;

import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DungeonAdditionsTab{
    public static CreativeTabs ALL = new CustomCreativeTab(CreativeTabs.getNextID(), "dungeon_additions", () -> ModItems.FLAME_HELMET);
    public static CreativeTabs BLOCKS = new CustomCreativeTab(CreativeTabs.getNextID(), "dungeon_additions_blocks", () -> Item.getItemFromBlock(ModBlocks.CITY_PILLAR_CHISLED));
}
