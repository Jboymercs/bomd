package com.dungeon_additions.da.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabItems extends CreativeTabs {


    public CreativeTabItems(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return null;
    }
}
