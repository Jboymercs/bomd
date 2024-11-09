package com.dungeon_additions.da.integration;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration {

    public static boolean IS_NETHER_API_LOADED = Loader.isModLoaded("nether_api");
    public static boolean IS_NETHER_BACKPORT_LOADED = Loader.isModLoaded("nb");
    public static boolean IS_DEEPER_DEPTHS_LOADED = Loader.isModLoaded("deeperdepths");
}
