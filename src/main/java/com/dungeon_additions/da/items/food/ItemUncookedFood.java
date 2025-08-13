package com.dungeon_additions.da.items.food;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.item.ItemFood;

public class ItemUncookedFood extends ItemFood implements IHasModel {

    public ItemUncookedFood(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setTranslationKey(name);
        setRegistryName(name);
        ModItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
