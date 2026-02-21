package com.dungeon_additions.da.integration;

import com.dungeon_additions.da.config.CompatConfig;
import com.dungeon_additions.da.config.ModConfig;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration {

    public static boolean IS_NETHER_BACKPORT_LOADED = Loader.isModLoaded("nb");
    public static boolean IS_NETHERIZED_BACKPORT_LOADED = Loader.isModLoaded("netherized");
    public static boolean IS_DEEPER_DEPTHS_LOADED = Loader.isModLoaded("deeperdepths");

    public static boolean IS_AETHER_LOADED = Loader.isModLoaded("aether_legacy");

    public static boolean IS_AETHER_CONTINUATION_LOADED = Loader.isModLoaded("aether_legacy_addon");

    public static boolean IS_AETHER_LOST_CONTENT_LOADED = Loader.isModLoaded("lost_aether");
}
