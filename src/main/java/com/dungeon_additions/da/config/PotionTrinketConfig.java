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

    @Config.Name("Fiery Respite Attack Speed Boost Amount")
    @Config.Comment("Change the attack speed boost when using Fiery Respite")
    @Config.RequiresMcRestart
    public static double fiery_respite_attack_speed = 0.3;

    @Config.Name("Fiery Respite Fire Damage Time")
    @Config.Comment("Change how long a target is afflicted with fire when the potion Fiery Respite is active. In seconds")
    @Config.RequiresMcRestart
    public static int fiery_respite_fire_time = 7;

    @Config.Name("Poison Garnish Increased Damage Amount")
    @Config.Comment("Change when Poison Garnish is active, how much extra damage is done to a target when the target has Poison active")
    @Config.RequiresMcRestart
    public static double poison_garnish_additive_damage = 1;

    @Config.Name("Poison Garnish Increased Damage Taken Amount")
    @Config.Comment("Change how much extra damage the player takes when having Poison Garnish active")
    @Config.RequiresMcRestart
    public static double poison_garnish_debuff_damage = 3;

    @Config.Name("Poison Garnish Double Poison Buff")
    @Config.Comment("When the target is below half health and hit with this potion active. Does it give the target Poison 2 instead of 1.")
    @Config.RequiresMcRestart
    public static boolean poison_garnish_buff = true;

}
