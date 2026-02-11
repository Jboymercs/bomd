package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/compatibility_config")
public class CompatConfig {

    @Config.Name("Aether Content Compatibility")
    @Config.Comment("If Aether Continuation, Aether Legacy, and Aether Lost Content are installed, will they change the High Court City structure when enabled.")
    @Config.RequiresMcRestart
    public static boolean aether_compat = true;

    @Config.Name("Deeper Depths Compatibility")
    @Config.Comment("If Deeper Depths is installed, It will change the Void Blossom Cave, and one of the variants of the Night Lich's Tower structures when enabled.")
    @Config.RequiresMcRestart
    public static boolean deeper_depths_compat = true;

    @Config.Name("Unseen's Nether Backport Compatibility")
    @Config.Comment("If Unseen's Nether Backport is installed, It will change the Burning Flame Arena's structure when enabled.")
    @Config.RequiresMcRestart
    public static boolean nether_backport_compat = true;

    @Config.Name("Baubles Compatibility")
    @Config.Comment("If Baubles is installed, It will change so that Trinkets can only use Bauble slots when enabled.")
    @Config.RequiresMcRestart
    public static boolean baubles_compat = true;




}
