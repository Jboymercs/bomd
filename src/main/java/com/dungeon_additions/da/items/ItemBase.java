package com.dungeon_additions.da.items;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name, CreativeTabs tab) {
        setTranslationKey(name);
        setRegistryName(name);
        if (tab != null) {
            setCreativeTab(tab);
        }

        ModItems.ITEMS.add(this);
    }

    public ItemBase(String name) {
        this(name, CreativeTabs.MISC);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
