package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "UnseensDungeonAdditions/potions_trinknets_config")
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

    @Config.Name("Faltered Damage Additive Amount")
    @Config.Comment("When applied to you or any entity, they will take x percentage of increased damage when applied when being Faltered")
    @Config.RequiresMcRestart
    public static double faltering_damage_increase = 0.3;

    @Config.Name("Default Player Falter Resistance")
    @Config.Comment("Change the default starting amount of Faltering resistance the player has.")
    @Config.RequiresMcRestart
    public static double player_default_falter_resistance = 0.5;

    @Config.Name("Player Armor Falter Additive")
    @Config.Comment("Per one armor point on the player, they will be added x amount of Falter Resistance.")
    @Config.RequiresMcRestart
    public static double armor_additive_falter_resistance = 0.04;

    @Config.Name("Player Armor Toughness Falter Additive")
    @Config.Comment("Per one armor toughness point on the player, they will be added x amount of Falter Resistance.")
    @Config.RequiresMcRestart
    public static double armor_additive_falter_resistance_t = 0.02;

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

    @Config.Name("Deaths Prosper Chance")
    @Config.Comment("Change Deaths Prosper chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int deaths_prosper_chance = 7;

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

    @Config.Name("Magic Charm Chance")
    @Config.Comment("Change Magic Charm chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int magic_charm_chance = 10;

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

    @Config.Name("Vampire Trinket Chance")
    @Config.Comment("Change Vampire Trinket chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int vampire_trinket_chance = 5;

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

    @Config.Name("Chip of Fortunate Chance")
    @Config.Comment("Change Chip of Fortunate chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int chip_fortunate_chance = 3;

    @Config.Name("Poison Touch Durability")
    @Config.Comment("Change Poison Touch item durability.")
    @Config.RequiresMcRestart
    public static int poison_touch_durability = 96;

    @Config.Name("Poison Touch Chance")
    @Config.Comment("Change Poison Touch chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int poison_touch_chance = 10;

    @Config.Name("Void's Durability Item Durability")
    @Config.Comment("Change Void's Durability item durability.")
    @Config.RequiresMcRestart
    public static int voids_item_durability = 64;

    @Config.Name("Storm Calling Item Durability")
    @Config.Comment("Change Storm Calling item durability.")
    @Config.RequiresMcRestart
    public static int storm_calling_durability = 128;

    @Config.Name("Storm Calling Cooldown")
    @Config.Comment("Change Storm Calling item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int storm_calling_cooldown = 20;

    @Config.Name("Spiral Vain Item Durability")
    @Config.Comment("Change Spiral Vain item durability.")
    @Config.RequiresMcRestart
    public static int spiral_vain_durability = 84;

    @Config.Name("Spiral Vain Chance")
    @Config.Comment("Change Spiral Vain chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int spiral_vain_chance = 10;

    @Config.Name("Cult Classic Item Durability")
    @Config.Comment("Change Cult Classic item durability.")
    @Config.RequiresMcRestart
    public static int cult_classic_durability = 84;

    @Config.Name("Cult Classic Chance")
    @Config.Comment("Change Cult Classic chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int cult_classic_chance = 3;

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

    @Config.Name("Golden Mark Chance")
    @Config.Comment("Change Golden Mark chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int golden_mark_chance = 15;

    @Config.Name("Golden Ritual Item Durability")
    @Config.Comment("Change Golden Ritual item durability.")
    @Config.RequiresMcRestart
    public static int golden_ritual_durability = 64;

    @Config.Name("Golden Ritual Damage")
    @Config.Comment("Change Golden Ritual damage.")
    @Config.RequiresMcRestart
    public static float golden_ritual_damage = 5;

    @Config.Name("Golden Ritual Cooldown")
    @Config.Comment("Change Golden Ritual item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int golden_ritual_cooldown = 60;

    @Config.Name("Frozen Crystal Item Durability")
    @Config.Comment("Change Frozen Crystal item durability.")
    @Config.RequiresMcRestart
    public static int frozen_crystal_durability = 128;

    @Config.Name("Frozen Crystal Damage Deduction")
    @Config.Comment("Change Frozen Crystal damage deduction amount as a percentage.")
    @Config.RequiresMcRestart
    public static double frozen_crystal_damage_deduction = 0.5;

    @Config.Name("Frozen Crystal Chance")
    @Config.Comment("Change Frozen Crystal chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int frozen_crystal_chance = 15;

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

    @Config.Name("Blood Stained Arrow Chance")
    @Config.Comment("Change Blood Stained Arrow chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int arrow_trinket_chance = 15;

    @Config.Name("Victory Rush Durability")
    @Config.Comment("Change Victory Rush item durability.")
    @Config.RequiresMcRestart
    public static int victory_rush_durability = 96;

    @Config.Name("Victory Rush Chance")
    @Config.Comment("Change Victory Rush chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int victory_rush_chance = 5;

    @Config.Name("Lost Dagger of Roh Durability")
    @Config.Comment("Change Last Dagger of Roh item durability.")
    @Config.RequiresMcRestart
    public static int dagger_trinket_durability = 84;

    @Config.Name("Lost Dagger of Roh Chance")
    @Config.Comment("Change Lost Dagger of Roh chance for happening.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 99)
    public static int dagger_trinket_chance = 4;

    @Config.Name("Blink Bolt Durability")
    @Config.Comment("Change Blink Bolt item durability.")
    @Config.RequiresMcRestart
    public static int teleport_trinket_durability = 246;

    @Config.Name("Blink Bolt Cooldown")
    @Config.Comment("Change Blink Bolt item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int blink_bolt_cooldown = 10;

    @Config.Name("Breath of the Wind Durability")
    @Config.Comment("Change Breath of the Wind item durability.")
    @Config.RequiresMcRestart
    public static int dodge_trinket_durability = 96;

    @Config.Name("Breath of the Wind Cooldown")
    @Config.Comment("Change Breath of the Wind item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int dodge_trinket_cooldown = 7;

    @Config.Name("Rotten Ring Durability")
    @Config.Comment("Change Rotten Ring item durability.")
    @Config.RequiresMcRestart
    public static int rotten_ring_durability = 64;

    @Config.Name("Rotten Ring Damage")
    @Config.Comment("Change Rotten Ring item ability damage.")
    @Config.RequiresMcRestart
    public static float rotten_ring_damage = 9;

    @Config.Name("Rotten Ring Cooldown")
    @Config.Comment("Change Rotten Ring item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int rotten_ring_cooldown = 30;

    @Config.Name("Cosmic Prowess Durability")
    @Config.Comment("Change Cosmic Prowess item durability.")
    @Config.RequiresMcRestart
    public static int blue_trinket_durability = 236;

    @Config.Name("Cosmic Prowess Damage")
    @Config.Comment("Change Cosmic Prowess item ability damage.")
    @Config.RequiresMcRestart
    public static float blue_trinket_damage = 6;

    @Config.Name("Cosmic Prowess Cooldown")
    @Config.Comment("Change Cosmic Prowess item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int cosmic_prowess_cooldown = 20;

    @Config.Name("Team Griefer Durability")
    @Config.Comment("Change Team Griefer item durability.")
    @Config.RequiresMcRestart
    public static int flame_explosion_trinket_durability = 100;

    @Config.Name("Team Griefer Damage")
    @Config.Comment("Change Team Griefer item ability damage.")
    @Config.RequiresMcRestart
    public static float flame_explosion_trinket_damage = 7;

    @Config.Name("Team Griefer Cooldown")
    @Config.Comment("Change Team Grifer item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int flame_explosion_cooldown = 40;

    @Config.Name("Long Legs Durability")
    @Config.Comment("Change Long Legs item durability.")
    @Config.RequiresMcRestart
    public static int endermen_trinket_durability = 736;

    @Config.Name("Long Legs Damage")
    @Config.Comment("Change how much speed is added by Long Legs trinket.")
    @Config.RequiresMcRestart
    public static double endermen_trinket_speed_amount = 0.07;

    @Config.Name("Metal Tornado Durability")
    @Config.Comment("Change Metal Tornado item durability.")
    @Config.RequiresMcRestart
    public static int mace_trinket_durability = 150;

    @Config.Name("Metal Tornado Damage")
    @Config.Comment("Change Metal Tornado item ability damage.")
    @Config.RequiresMcRestart
    public static float mace_trinket_damage = 8;

    @Config.Name("Metal Tornado Cooldown")
    @Config.Comment("Change Metal Tornado item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int metal_tornado_cooldown = 25;

    @Config.Name("Thorn Ring Durability")
    @Config.Comment("Change Thorn Ring item durability.")
    @Config.RequiresMcRestart
    public static int thorn_ring_trinket_durability = 236;

    @Config.Name("Thorn Ring Damage")
    @Config.Comment("Change Thorn Ring item ability damage.")
    @Config.RequiresMcRestart
    public static float thorn_ring_trinket_damage = 6;

    @Config.Name("Thorn Ring Cooldown")
    @Config.Comment("Change Thorn Ring item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int thorn_ring_cooldown = 15;

    @Config.Name("Last Stand Durability")
    @Config.Comment("Change Last Stand item durability.")
    @Config.RequiresMcRestart
    public static int last_stand_trinket_durability = 20;

    @Config.Name("Last Stand Cooldown")
    @Config.Comment("Change Last Stand item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int last_stand_cooldown = 300;

    @Config.Name("Goat Tenacity Durability")
    @Config.Comment("Change Goat Tenacity item durability.")
    @Config.RequiresMcRestart
    public static int goat_tenacity_durability = 42;

    @Config.Name("Goat Tenacity Falter Resistance")
    @Config.Comment("Change how much Goat's Tenacity adds to the players falter resistance")
    @Config.RequiresMcRestart
    public static double goat_tenacity_resistance =  0.6;

    @Config.Name("Pocket Pistol Damage")
    @Config.Comment("Change Pocket Pistol item ability damage.")
    @Config.RequiresMcRestart
    public static float pocket_pistol_damage = 13;

    @Config.Name("Pocket Pistol Durability")
    @Config.Comment("Change Pocket Pistol item durability.")
    @Config.RequiresMcRestart
    public static int pocket_pistol_durability = 128;

    @Config.Name("Pocket Pistol Cooldown")
    @Config.Comment("Change Pocket Pistol item cooldown. In seconds.")
    @Config.RequiresMcRestart
    public static int pocket_pistol_cooldown = 20;

    @Config.Name("Trinkets become unbreakable Enable/Disable")
    @Config.Comment("Change if all trinkets in the mod become unbreakable and do not take durability damage. default false.")
    @Config.RequiresMcRestart
    public static boolean trinkets_unbreakable = false;

    @Config.Name("Max Trinkets Allowed")
    @Config.Comment("Change how many max any asortment of trinkets the player can carry from this mod. Change this if you plan on changing slots to make them more accessible. default : 4")
    @Config.RequiresMcRestart
    public static int max_trinkets_allowed = 4;

}
