package com.dungeon_additions.da.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.PETROGLOOM, new ItemStack(ModBlocks.PETROGLOOM_ROUGH, 1), 0.1F);
        GameRegistry.addSmelting(ModItems.UNCOOKED_BEETLE_MORSEL, new ItemStack(ModItems.COOKED_BEETLE_MORSEL, 1), 0.2F);
    }
}
