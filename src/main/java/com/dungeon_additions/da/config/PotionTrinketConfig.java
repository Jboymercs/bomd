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

    @Config.Name("Glass Cannon Trinket Durability")
    @Config.Comment("Change Glass Cannon item durability.")
    @Config.RequiresMcRestart
    public static int glass_cannon_durability = 128;

    @Config.Name("Speed Runner Trinket Durability")
    @Config.Comment("Change Speed Runner item durability.")
    @Config.RequiresMcRestart
    public static int speed_runner_durability = 128;

    @Config.Name("Flame's Rage Trinket Durability")
    @Config.Comment("Change Flame's Rage item durability.")
    @Config.RequiresMcRestart
    public static int flames_rage_durability = 912;

    @Config.Name("Flame's Rage Damage")
    @Config.Comment("Change Flame's Rage default damage.")
    @Config.RequiresMcRestart
    public static int flames_rage_default_damage = 2;

    @Config.Name("Flame's Rage On Fire Damage")
    @Config.Comment("Change Flame's Rage on fire damage.")
    @Config.RequiresMcRestart
    public static int flames_rage_on_fire_damage = 4;

    @Config.Name("Death's Prosper Trinket Durability")
    @Config.Comment("Change Death's Prosper item durability.")
    @Config.RequiresMcRestart
    public static int deaths_prosper_durability = 96;

    @Config.Name("Death's Prosper Heal Amount")
    @Config.Comment("Change Death's Prosper heal amount.")
    @Config.RequiresMcRestart
    public static int deaths_prosper_heal_amount = 4;

    @Config.Name("Mythic Shield Trinket Durability")
    @Config.Comment("Change Mythic Shield item durability.")
    @Config.RequiresMcRestart
    public static int mythic_shield_durability = 736;

    @Config.Name("Mythic Shield Trinket Armor")
    @Config.Comment("Change Mythic Shield Armor percentage added based off of total armor of the player.")
    @Config.RequiresMcRestart
    public static double mythic_shield_armor_value = 0.15;

    @Config.Name("Exalted Shield Trinket Durability")
    @Config.Comment("Change Exalted Shield item durability.")
    @Config.RequiresMcRestart
    public static int exalted_shield_durability = 736;

    @Config.Name("Exalted Shield Trinket Armor")
    @Config.Comment("Change Exalted Shield Armor percentage added based off of total armor of the player.")
    @Config.RequiresMcRestart
    public static double exalted_shield_armor_value = 0.1;

    @Config.Name("Creeper's Will Trinket Durability")
    @Config.Comment("Change Creeper's Will item durability.")
    @Config.RequiresMcRestart
    public static int creepers_will_durability = 32;

    @Config.Name("Creeper's Will Damage")
    @Config.Comment("Change Creeper's Will damage when exploding.")
    @Config.RequiresMcRestart
    public static float creepers_will_damage = 13;

    @Config.Name("Magic Charm Trinket Durability")
    @Config.Comment("Change Magic Charm item durability.")
    @Config.RequiresMcRestart
    public static int magic_charm_durability = 128;

    @Config.Name("Magic Charm Spear Damage")
    @Config.Comment("Change Magic Charm spear damage.")
    @Config.RequiresMcRestart
    public static float magic_charm_spear_damage = 8;

    @Config.Name("Frozen Slam Trinket Durability")
    @Config.Comment("Change Frozen Slam item durability.")
    @Config.RequiresMcRestart
    public static int frozen_slam_durability = 128;

    @Config.Name("Frozen Slam Ice Damage")
    @Config.Comment("Change Frozen Slam ice damage.")
    @Config.RequiresMcRestart
    public static float frozen_slam_ice_damage = 10;

    @Config.Name("Vampiric Trinket Durability")
    @Config.Comment("Change Vampiric item durability.")
    @Config.RequiresMcRestart
    public static int vampiric_durability = 96;

    @Config.Name("Vigorous Journey Durability")
    @Config.Comment("Change Vigorous Journey item durability.")
    @Config.RequiresMcRestart
    public static int vigorous_journey_durability = 636;

    @Config.Name("Vigorous Journey Health Amount")
    @Config.Comment("Change Vigorous Journey health that is added to the player. In a percentage.")
    @Config.RequiresMcRestart
    public static double vigorous_journey_health_amount = 0.2;

    @Config.Name("Chip of Fortunate Durability")
    @Config.Comment("Change Chip of Fortunate item durability.")
    @Config.RequiresMcRestart
    public static int chip_fortunate_durability = 46;

    @Config.Name("Poison Touch Durability")
    @Config.Comment("Change Poison Touch item durability.")
    @Config.RequiresMcRestart
    public static int poison_touch_durability = 96;

    @Config.Name("Void's Durability Item Durability")
    @Config.Comment("Change Void's Durability item durability.")
    @Config.RequiresMcRestart
    public static int voids_item_durability = 64;

    @Config.Name("Storm Calling Item Durability")
    @Config.Comment("Change Storm Calling item durability.")
    @Config.RequiresMcRestart
    public static int storm_calling_durability = 128;

    @Config.Name("Spiral Vain Item Durability")
    @Config.Comment("Change Spiral Vain item durability.")
    @Config.RequiresMcRestart
    public static int spiral_vain_durability = 84;

    @Config.Name("Cult Classic Item Durability")
    @Config.Comment("Change Cult Classic item durability.")
    @Config.RequiresMcRestart
    public static int cult_classic_durability = 84;

    @Config.Name("Party Starter Item Durability")
    @Config.Comment("Change Party Starter item durability.")
    @Config.RequiresMcRestart
    public static int party_starter_durability = 64;

    @Config.Name("Stalwart Summoner Item Durability")
    @Config.Comment("Change Stalwart Summoner item durability.")
    @Config.RequiresMcRestart
    public static int stalwart_summoner_durability = 32;

    @Config.Name("Golden Mark Item Durability")
    @Config.Comment("Change Golden Mark item durability.")
    @Config.RequiresMcRestart
    public static int golden_mark_durability = 128;

    @Config.Name("Golden Ritual Item Durability")
    @Config.Comment("Change Golden Ritual item durability.")
    @Config.RequiresMcRestart
    public static int golden_ritual_durability = 64;

    @Config.Name("Golden Ritual Damage")
    @Config.Comment("Change Golden Ritual damage.")
    @Config.RequiresMcRestart
    public static float golden_ritual_damage = 5;

    @Config.Name("Frozen Crystal Item Durability")
    @Config.Comment("Change Frozen Crystal item durability.")
    @Config.RequiresMcRestart
    public static int frozen_crystal_durability = 128;

    @Config.Name("Frozen Crystal Damage Deduction")
    @Config.Comment("Change Frozen Crystal damage deduction amount as a percentage.")
    @Config.RequiresMcRestart
    public static double frozen_crystal_damage_deduction = 0.5;

    @Config.Name("Magic Reservoir Item Durability")
    @Config.Comment("Change Magic Reservoir item durability.")
    @Config.RequiresMcRestart
    public static int magic_reservoir_durability = 636;

    @Config.Name("Magic Reservoir Damage")
    @Config.Comment("Change Magic Reservoir damage that boosts magic attacks.")
    @Config.RequiresMcRestart
    public static float magic_reservoir_damage = 1;

    @Config.Name("Blood Stained Arrow Durability")
    @Config.Comment("Change Blood Stained Arrow item durability.")
    @Config.RequiresMcRestart
    public static int blood_stained_arrow_durability = 46;

}
