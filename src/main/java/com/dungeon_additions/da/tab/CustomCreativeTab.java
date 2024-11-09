package com.dungeon_additions.da.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class CustomCreativeTab extends CreativeTabs {
    Supplier<Item> icon;

    public CustomCreativeTab(int index, String label, Supplier<Item> icon) {
        super(index, label);
        this.icon = icon;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(icon.get());
    }
}
