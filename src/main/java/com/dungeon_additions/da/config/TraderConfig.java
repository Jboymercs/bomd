package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/trader_config")
public class TraderConfig {


    @Config.RequiresMcRestart
    @Config.Comment(value = "What Items can be given with a Copper Trader Coin. Works with 'modID:itemName'.")
    public static String[] copper_tier_trade_list = new String[] {

    };

    @Config.RequiresMcRestart
    @Config.Comment(value = "What Items can be given with a Silver Trader Coin. Works with 'modID:itemName'.")
    public static String[] silver_tier_trade_list = new String[] {

    };

    @Config.RequiresMcRestart
    @Config.Comment(value = "What Items can be given with a Golden Trader Coin. Works with 'modID:itemName'.")
    public static String[] golden_tier_trade_list = new String[] {

    };

    @Config.Name("Mysterious Trader follows Emerald block")
    @Config.Comment("Does the Mysterious Trader follow players with an Emerald block?")
    @Config.RequiresMcRestart
    public static boolean trader_follow_players = true;
}
