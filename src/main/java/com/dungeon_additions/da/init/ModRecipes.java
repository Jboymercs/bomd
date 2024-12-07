package com.dungeon_additions.da.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.PETROGLOOM, new ItemStack(ModBlocks.PETROGLOOM_ROUGH, 1), 0.1F);
    }
}
