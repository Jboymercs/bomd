package com.dungeon_additions.da.util;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModReference {
    public static final String MOD_ID ="da";
    public static final String NAME = "Dungeon Additions";
    public static final String CHANNEL_NETWORK_NAME = "DungeonAdditions";

    public static final String VERSION = "0.1.5-1";

    public static final String CLIENT_PROXY_CLASS = "com.dungeon_additions.da.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.dungeon_additions.da.proxy.CommonProxy";


    public static final Logger LOGGER = LogManager.getLogger(ModReference.MOD_ID);
}
