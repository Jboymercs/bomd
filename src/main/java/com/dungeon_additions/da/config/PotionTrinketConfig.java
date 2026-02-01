package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/potions_trinknets_config")
public class PotionTrinketConfig {


    @Config.Name("Golden Devotion Physical Damage Reduction Amount")
    @Config.Comment("Change what percentage of damage is reduced when Golden Devotion is active and the player takes damage from an entity")
    @Config.RequiresMcRestart
    public static double golden_devotion_reduction_amount = 0.15;

    @Config.Name("Golden Devotion Health Boost Amount")
    @Config.Comment("Change the percentage of Max Health the player is given when using Golden Devotion")
    @Config.RequiresMcRestart
    public static double golden_devotion_health_boost = 0.4;

    @Config.Name("Golden Devotion Heavy Weapon Threshold")
    @Config.Comment("How much damage should the base weapon be capable of doing before Golden Devotion's attack buff is added")
    @Config.RequiresMcRestart
    public static double golden_devotion_slow_weapons_threshold = 9;


    @Config.Name("Golden Vow Damage Additive Amount")
    @Config.Comment("Change what percentage of damage is added when Golden Vow is active and the player damages an entity")
    @Config.RequiresMcRestart
    public static double golden_vow_additive_amount = 0.2;

    @Config.Name("Golden Vow Speed Bonus")
    @Config.Comment("Change what percentage of speed the player is given when using Golden Vow")
    @Config.RequiresMcRestart
    public static double golden_vow_speed_amount = 0.15;

    @Config.Name("Hunters Mark Damage Additive Amount")
    @Config.Comment("When applied to you or any entity, they will take x percentage of increased damage when applied with Hunters Mark")
    @Config.RequiresMcRestart
    public static double hunters_mark_damage_increase = 0.1;

}
