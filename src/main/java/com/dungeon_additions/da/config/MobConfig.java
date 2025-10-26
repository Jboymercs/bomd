package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/mob_config")
public class MobConfig {
    @Config.Name("Void Blossom Health")
    @Config.Comment("Change the health of the Void Blossom boss")
    @Config.RequiresMcRestart
    public static double blossom_health = 350;

    @Config.Name("Void Blossom Armor")
    @Config.Comment("Change the armor value of the Void Blossom")
    @Config.RequiresMcRestart
    public static double blossom_armor = 4;

    @Config.Name("Void Blossom Attack Cooldown One")
    @Config.Comment("Change the attack cooldown of the Void Blossom in seconds with it having above 50% health")
    @Config.RequiresMcRestart
    public static int blossom_cooldown_one = 6;

    @Config.Name("Void Blossom Attack Cooldown Two")
    @Config.Comment("Change the attack cooldown of the Void Blossom in seconds with it having below 50% health")
    @Config.RequiresMcRestart
    public static int blossom_cooldown_two = 5;

    @Config.Name("Void Blossom Cooldown Multiplayer")
    @Config.Comment("Change the cooldown degradation per player not counting the first one. This basically makes the cooldown faster when there are more players around, in ticks")
    @Config.RequiresMcRestart
    public static int blossom_degradation_cooldown = 8;

    @Config.Name("Void Blossom New Attacks Enable/Disalbe")
    @Config.Comment("For those that don't want minions to spawn during the fight, when set to false minions will not spawn from the void blossom")
    @Config.RequiresMcRestart
    public static boolean enable_minions = true;

    @Config.Name("Void Blossom Attack Damage")
    @Config.Comment("Change the Attack Damage of the Void Blossom")
    @Config.RequiresMcRestart
    public static double blossom_attack_damange = 12;

    @Config.Name("Void Blossom Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Void Blossom. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float blossom_damage_cap = 25;

    @Config.Name("Void Blossom Max Minions")
    @Config.Comment("Change how many Mini Void Blossoms can be active for the Void Blossom. Default: 2")
    @Config.RequiresMcRestart
    public static double blossom_max_minion_count = 2;

    @Config.Name("Void Blossom Spikes Lag Reducer")
    @Config.Comment("When enabled, the Void spikes will only spawn within 20 blocks of all players, to help reduce lag when being spawned")
    @Config.RequiresMcRestart
    public static boolean spike_lag_reducer = false;

    @Config.Name("Obsidilith Health")
    @Config.Comment("Change the health of the Obsidilith boss")
    @Config.RequiresMcRestart
    public static double obsidilith_health = 300;

    @Config.Name("Obsidilith Armor")
    @Config.Comment("Change the armor of the Obsidilith")
    @Config.RequiresMcRestart
    public static double obsidilith_armor = 18;

    @Config.Name("Obsidilith Armor Toughness")
    @Config.Comment("Change the armor toughness of the Obsidilith")
    @Config.RequiresMcRestart
    public static double obsidilith_armor_toughness = 4;

    @Config.Name("Obsidilith Attack Damage")
    @Config.Comment("Change the attack damage of the Obsidilith")
    @Config.RequiresMcRestart
    public static double obsidilith_attack_damage = 22;

    @Config.Name("Obsidilith Experience Drop")
    @Config.Comment("Take in mind, this boss drops x number of experience 16 times, you can change the value of the given orb summoned below")
    @Config.RequiresMcRestart
    public static int obsidilith_experience_orb_value = 14;

    @Config.Name("Obsidilith Cooldown Multiplayer")
    @Config.Comment("Change the cooldown degradation per player not counting the first one. This basically makes the cooldown faster when there are more players around, in ticks")
    @Config.RequiresMcRestart
    public static int obsidilith_degradation_cooldown = 8;

    @Config.Name("Obsidilith Cooldown")
    @Config.Comment("Change the cooldown of the Obsidilith boss while it is not in a Shielded state, in seconds.")
    @Config.RequiresMcRestart
    public static double obsidilith_cooldown = 5.5;

    @Config.Name("Obsidilith Cooldown Shielded")
    @Config.Comment("Change the cooldown of the Obsidilith boss while its in a Shielded state, in seconds")
    @Config.RequiresMcRestart
    public static double obsidilith_cooldown_shielded = 10;

    @Config.Name("Obsidilith Two Part Boss")
    @Config.Comment("Change if the Obsidilith summons the Voidclysm after it is defeated")
    @Config.RequiresMcRestart
    public static boolean obsidilith_two_part_boss = true;

    @Config.Name("Obsidilith Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Obsidilith. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float obsidilith_damage_cap = 29;

    @Config.Name("Voidclysm Health")
    @Config.Comment("Change the Health of the Voidclysm")
    @Config.RequiresMcRestart
    public static double voidclysm_health = 400;

    @Config.Name("Voidclysm Attack Damage")
    @Config.Comment("Change the attack damage of the Voidclysm")
    @Config.RequiresMcRestart
    public static double voidclysm_attack_damage = 26;

    @Config.Name("Voidclysm Armor")
    @Config.Comment("Change the armor value of the Voidclysm")
    @Config.RequiresMcRestart
    public static double voidclysm_armor = 12;

    @Config.Name("Voicvlysm Armor Toughness")
    @Config.Comment("Change the armor toughness of the Voidclysm")
    @Config.RequiresMcRestart
    public static double voidclysm_armor_toughness = 6;

    @Config.Name("Voidclysm Attack Speed")
    @Config.Comment("Change the attack speed in ticks for the Voidclysm, this value halves when reaching below half health.")
    @Config.RequiresMcRestart
    public static int voidclysm_attack_cooldown = 20;

    @Config.Name("Voidclysm Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Voidclysm. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float voidclysm_damage_cap = 30;

    @Config.Name("Voidclysm Grab Attack")
    @Config.Comment("Change how much Health Voidclysm removes from the players Health bar in percentage. default 50% Health")
    @Config.RequiresMcRestart
    public static double voidclysm_grab_attack_damage = 0.5;

    @Config.Name("Voidclysm Experience Value")
    @Config.Comment("Change how much experience the Voidclysm drops upon death.")
    @Config.RequiresMcRestart
    public static int voidclysm_experience_value = 350;

    @Config.Name("Voidclysm Block Cooldown")
    @Config.Comment("How long before the Voidclysm can attempt to block an attack if its currently not attack doing an attack. In seconds")
    @Config.RequiresMcRestart
    public static int voidclysm_block_cooldown = 5;

    @Config.Name("Mini Void Blossom Health")
    @Config.Comment("Change the health of the Mini Void Blossom")
    @Config.RequiresMcRestart
    public static double mini_blossom_health = 40;

    @Config.Name("Mini Void Blossom Attack Damage")
    @Config.Comment("Change the attack damage of Mini Void Blossom")
    @Config.RequiresMcRestart
    public static float mini_blossom_attack_damage = 8;

    @Config.Name("Nether Abberrant Health")
    @Config.Comment("Change the health of the Nether Abberrant")
    @Config.RequiresMcRestart
    public static double abberrant_health = 35;

    @Config.Name("Nether Abberrant Attack Damage")
    @Config.Comment("Change the attack damage of the Nether Abberrant")
    @Config.RequiresMcRestart
    public static double abberrant_damage = 12;

    @Config.Name("Incendium Attack Damage")
    @Config.Comment("Change the Attack Damage of the Incendium Spirit")
    @Config.RequiresMcRestart
    public static double incendium_attack_damage = 14;

    @Config.Name("Incendium Health")
    @Config.Comment("Change the Health of the Incendium Spirit")
    @Config.RequiresMcRestart
    public static double incendium_health = 45;

    @Config.Name("Incendium Armor")
    @Config.Comment("Change the Armor Value of the Incendium Spirit")
    @Config.RequiresMcRestart
    public static double incendium_armor = 16;

    @Config.Name("Bareant Spirit Health")
    @Config.Comment("Change the Health of the Bareant Spirit")
    @Config.RequiresMcRestart
    public static double bareant_health = 20;

    @Config.Name("Bareant Spirit Attack Damage")
    @Config.Comment("Change the Attack Damage of the Bareant Spirit")
    @Config.RequiresMcRestart
    public static double bareant_attack_damage = 16;

    @Config.Name("Bareant Spirit Armor")
    @Config.Comment("Change the Armor of the Bareant Spirit")
    @Config.RequiresMcRestart
    public static double bareant_armor = 10;

    @Config.Name("Volactile Spirit Inspired Damage Multiplier")
    @Config.Comment("When Volactile raises its sword and buffs itself, it does base damage times this multiplier for melee attacks")
    @Config.RequiresMcRestart
    public static double volactile_inspired_multiplier = 1.4;

    @Config.Name("Volactile Spirit Health")
    @Config.Comment("Change the Health of the Volactile Spirit")
    @Config.RequiresMcRestart
    public static double volactile_spirit_health = 210;

    @Config.Name("Volactile Armor")
    @Config.Comment("Change the Armor of the Volactile Spirit")
    @Config.RequiresMcRestart
    public static double volactile_armor = 16;

    @Config.Name("Volactile Armor Toughness")
    @Config.Comment("Change the Armor Toughness of the Volactile Spirit")
    @Config.RequiresMcRestart
    public static double volactile_armor_toughness = 4;

    @Config.Name("Volactile Attack Damage")
    @Config.Comment("Change the Attack Damage of the Volactile Spirit")
    @Config.RequiresMcRestart
    public static double volactile_attack_damage = 22;

    @Config.Name("Volactile Projectile Resistance")
    @Config.Comment("When a projectile is fired at the Volactile Spirit, it will only take x percentage of the damage")
    @Config.RequiresMcRestart
    public static double volactile_projectile_resistance = 0.6;

    @Config.Name("Volactile Summoning Radius")
    @Config.Comment("Change how far the Flame orb to summon the Volactile Spirit detects for any dungeon mobs. if this check finds mobs, it will not summon the mini-boss")
    @Config.RequiresMcRestart
    public static double volactile_ritual_radius = 30;

    @Config.Name("Volactile Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Volactile Spirit. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float volactile_damage_cap = 25;

    @Config.Name("Knight of Burning Flame Armor")
    @Config.Comment("Change the armor value of the this boss (Knight of Burning Flame)")
    @Config.RequiresMcRestart
    public static double burning_knight_armor = 16;

    @Config.Name("Knight of Burning Flame Armor Toughness")
    @Config.Comment("Change the armor toughness value of this boss (Knight of Burning Flame)")
    @Config.RequiresMcRestart
    public static double burning_knight_armor_toughness = 8;

    @Config.Name("Knight of Burning Flame Attack Damage")
    @Config.Comment("Change the attack damage of this boss (Knight of Burning Flame)")
    @Config.RequiresMcRestart
    public static double burning_knight_damage = 26;

    @Config.Name("Knight of Burning Flame Health")
    @Config.Comment("Change the health of this boss (Knight of Burning Flame)")
    @Config.RequiresMcRestart
    public static double burning_knight_health = 390;

    @Config.Name("Knight of Burning Flame Block Cooldown")
    @Config.Comment("Change the timer when the Knight of Burning Flame can block attacks, take note this is only fired between cool downs of each attack in seconds")
    @Config.RequiresMcRestart
    public static double burning_knight_block_cooldown = 3.5;

    @Config.Name("Knight of Burning Flame Health Potion Amount")
    @Config.Comment("Change the amount of health healed when the Knight of Burning Flame uses a health potion. this is measured in X / Maxhealth. so .07 would be 7% of it's max health")
    @Config.RequiresMcRestart
    public static double burning_knight_heal_amount = 0.07;

    @Config.Name("Knight of Burning Flame Projectile Resistance")
    @Config.Comment("When the Knight of Burning Flame is hit with a projectile, it will take x percentage of the projectiles damage")
    @Config.RequiresMcRestart
    public static double burning_knight_projectile_resistance = 0.75;

    @Config.Name("Knight of Burning Flame Heal Action")
    @Config.Comment("Change if the Knight of Burning Flame can use healing potions, default: true")
    @Config.RequiresMcRestart
    public static boolean knight_enable_potion_use = true;

    @Config.Name("Knight of Burning Flame Health Potion Cooldown")
    @Config.Comment("Change the cooldown before the Knight of Burning Flame can use another Health Potion in seconds, take note! This boss is limited in how many potions it can use. Only using one per 25% Health if it gets a chance to use one")
    @Config.RequiresMcRestart
    public static int burning_knight_heal_cooldown = 30;

    @Config.Name("Knight of Burning Flame Let the world Burn")
    @Config.Comment("Do the Projectiles the nether knight shoots set ablaze when exploding")
    @Config.RequiresMcRestart
    public static boolean let_the_world_burn = true;

    @Config.Name("Knight of Burning Flame Attack Cooldown Long Distance")
    @Config.Comment("Change the Cooldown of the Burning Knight of Flame when from a longer distance not in a aggrivated state, in ticks (20 ticks = 1 second)")
    @Config.RequiresMcRestart
    public static int knight_cooldown_long_distance = 50;

    @Config.Name("Knight of Burning Flame Attack Cooldown Short Distance")
    @Config.Comment("Change the Cooldown of the Burning of Flame when from a short distance not in a aggrivated state, in ticks (20 ticks = 1 second")
    @Config.RequiresMcRestart
    public static int knight_cooldown_short_distance = 15;

    @Config.Name("Knight of Burning Flame Attack Cooldown Aggrivation State")
    @Config.Comment("Change the Cooldown of the Burning Knight of Flame for when in a aggrivated state, in ticks (20 ticks = 1 second")
    @Config.RequiresMcRestart
    public static int knight_aggrivate_state_cooldown = 10;

    @Config.Name("Knight of Burning Flame Experience Drop")
    @Config.Comment("Take in mind, this boss drops x number of experience 16 times, you can change the value of the given orb summoned below")
    @Config.RequiresMcRestart
    public static int kobf_experience_orb_value = 10;

    @Config.Name("Knight of Burning Flame AOE Damage")
    @Config.Comment("How much damage is dealt by the magma blocks popping out of the ground")
    @Config.RequiresMcRestart
    public static float aoe_block_damage = 15;

    @Config.Name("Knight of Burning Flame Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Knight of Burning Flame. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float kobf_damage_cap = 29;

    @Config.Name("Stormvier/Galvonizer Health")
    @Config.Comment("Change the Health of the Stormvier/Galvonizer")
    @Config.RequiresMcRestart
    public static double rot_knights_health = 30;

    @Config.Name("Stormvier/Galvonizer Armor")
    @Config.Comment("Change the Armor of the Stormvier/Galvonizer")
    @Config.RequiresMcRestart
    public static double rot_knights_armor = 6;

    @Config.Name("Stormvier/Galvonizer Attack Damage")
    @Config.Comment("Change the Attack Damage of the Stormvier/Galvonizer")
    @Config.RequiresMcRestart
    public static double rot_knights_attack_damage = 7;

    @Config.Name("Stormvier Fallen Health")
    @Config.Comment("Change the health of the Stormvier Fallen")
    @Config.RequiresMcRestart
    public static double fallen_health = 180;

    @Config.Name("Stormvier Fallen Attack Damage")
    @Config.Comment("Change the Attack Damage of the Stormvier Fallen")
    @Config.RequiresMcRestart
    public static double fallen_attack_damage = 10;

    @Config.Name("Stormvier Fallen Armor")
    @Config.Comment("Change the Armor value of the Stormvier Fallen")
    @Config.RequiresMcRestart
    public static double fallen_armor = 10;

    @Config.Name("Stormvier Fallen Attack Cooldown")
    @Config.Comment("Change the Cooldown value of the Stormvier Fallen in ticks (20 tick = 1 second)")
    @Config.RequiresMcRestart
    public static int fallen_cool_down = 50;

    @Config.Name("Stormvier Fallen Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Stormvier Fallen. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float fallen_damage_cap = 18;

    @Config.Name("Night Lich Active Mob Count")
    @Config.Comment("Change the active mob count the Night Lich can have, try to do it in multiples of 4")
    @Config.RequiresMcRestart
    public static int lich_active_mob_count = 12;

    @Config.Name("Night Lich Active Mob Count Multiplayer")
    @Config.Comment("Per player not including one, the Night Lich can have x additional mobs in active mob count. This does not affect the progressive cooldown when it reaches cap. Change to 0 to disable")
    @Config.RequiresMcRestart
    public static int lich_active_mob_count_multiplayer = 4;

    @Config.Name("Night Lich Active Mob Count Distance")
    @Config.Comment("Change the distance from the Lich for that mob too not be counted as an active mob")
    @Config.RequiresMcRestart
    public static int lich_active_mob_distance = 70;

    @Config.Name("Night Lich Cooldown static")
    @Config.Comment("Change the static cooldown of the Night Lich inbetween attacks, this is an unchanged cooldown in ticks (20 ticks = 1 second)")
    @Config.RequiresMcRestart
    public static int lich_static_cooldown = 15;

    @Config.Name("Night Lich Cooldown Progressive")
    @Config.Comment("In ticks change the progressive cooldown of the Night Lich, this basically increases the cooldown for each mob that is active and summoned by the Lich. EX: 2 active mobs * x = x * 2 + static cooldown. CAPS at active mob count")
    @Config.RequiresMcRestart
    public static int lich_progressive_cooldown = 5;

    @Config.Name("Night Lich Cooldown Additive")
    @Config.Comment("This value basically is another static, however it only adds onto the cooldown of certain attacks in areas, mostly between switching melee mode and ranged mode to give breathing room in seconds")
    @Config.RequiresMcRestart
    public static int lich_additive_cooldown = 3;

    @Config.Name("Night Lich Magic Missile Speed")
    @Config.Comment("Change the speed of the Magic Missile Projectile used by the Night Lich")
    @Config.RequiresMcRestart
    public static double magic_missile_velocity = 1.6;

    @Config.Name("Night Lich Magic Fireball Speed")
    @Config.Comment("Change the speed of the Magic Fireball Projectiles used by the Night Lich")
    @Config.RequiresMcRestart
    public static double magic_fireball_velocity = 1.4;

    @Config.Name("Night Lich Health")
    @Config.Comment("Change the Health of the Night Lich")
    @Config.RequiresMcRestart
    public static double night_lich_health = 470;

    @Config.Name("Night Lich Attack Damage")
    @Config.Comment("Change the Attack Damage of the Night Lich")
    @Config.RequiresMcRestart
    public static double night_lich_attack_damage = 26;

    @Config.Name("Night Lich Staff AOE Damage Multiplier")
    @Config.Comment("Change the damage done by the Lich's staffs in the AOE attacks, used by baseAttackDamage * x")
    @Config.RequiresMcRestart
    public static double night_lich_staff_multiplier = 1.25;

    @Config.Name("Night Lich Red Dash Damage Multiplier")
    @Config.Comment("Change the damage done by the Red Dash attack, used by baseAttackDamage * x")
    @Config.RequiresMcRestart
    public static double night_lich_dash_multiplier = 1.4;

    @Config.Name("Night Lich Armor")
    @Config.Comment("Change the armor value of the Night Lich")
    @Config.RequiresMcRestart
    public static double night_lich_armor = 12;

    @Config.Name("Night Lich Armor Toughness")
    @Config.Comment("Change the Night Lich Armor Toughness")
    @Config.RequiresMcRestart
    public static double night_lich_armor_toughness = 2;

    @Config.Name("Night Lich Angered State Damage Reduction")
    @Config.Comment("Night Lich has a resistance to all damage while in melee state, you can change that value here")
    @Config.RequiresMcRestart
    public static double lich_melee_resistance = 0.6;

    @Config.Name("Night Lich Experience Value")
    @Config.Comment("Change the Experience dropped by the Night Lich")
    @Config.RequiresMcRestart
    public static int lich_experience_value = 225;

    @Config.Name("Night Lich Teleport Cooldown Timer")
    @Config.Comment("Change how often the Night Lich can attempt to teleport away from projectiles while not in a melee state, and not casting a spell. In Seconds")
    @Config.RequiresMcRestart
    public static int lich_teleport_timer = 12;

    @Config.Name("Night Lich Mob Spawn List One")
    @Config.Comment("Add or remove possible mob spawns that the Night Lich can summon, must be ModID:entity_name")
    @Config.RequiresMcRestart
    public static String[] mob_list_one = {
            "da:frost_draugr",
            "da:frost_draugr_ranger",
            "minecraft:spider"
    };

    @Config.Name("Night Lich Mob Spawn List Two")
    @Config.Comment("Add or remove possible mob spawns that the Night Lich can summon, this list only takes effect when below 50% Health. Must be ModID:entity_name")
    @Config.RequiresMcRestart
    public static String[] mob_list_two = {
            "da:frost_draugr",
            "da:frost_draugr_ranger",
            "minecraft:cave_spider",
            "minecraft:blaze"
    };

    @Config.Name("Night Lich Enable Mob Gear")
    @Config.Comment("When enabled, Zombies or anything that extends off of zombies will be given gear such as an Iron Sword and Helmet")
    @Config.RequiresMcRestart
    public static boolean lich_enable_gear = true;

    @Config.Name("Night Lich Enable Disappearance")
    @Config.Comment("When enabled, the Night Lich will disappear and essentially be gone. Usually only occurs when it's daylight. This also disables the Night Lich forcing night time")
    @Config.RequiresMcRestart
    public static boolean lich_enable_daylight = true;

    @Config.Name("Night Lich Evening Disappearance Evening")
    @Config.Comment("Change the evening time for when the Night Lich can be summoned, take note that official day time is at 0")
    @Config.RequiresMcRestart
    public static int lich_summon_time = 12000;

    @Config.Name("Night Lich Flying Speed")
    @Config.Comment("Change the Night Lich's speed when in the air and not doing a spell or attack, this value might seem small but trust me its fast")
    @Config.RequiresMcRestart
    public static double lich_movement_speed = 0.1;

    @Config.Name("Night Lich Flying Speed Combat")
    @Config.Comment("Change the Night Lich's speed when doing certain attacks in the air, this will make the boss slower so its easier to hit")
    @Config.RequiresMcRestart
    public static double lich_movement_speed_combat = 0.055;

    @Config.Name("Night Lich Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Night Lich. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float night_lich_damage_cap = 35;

    @Config.Name("Frostborn Draugr Health")
    @Config.Comment("Change the Health of the Frostborn Draugr")
    @Config.RequiresMcRestart
    public static double draugr_health = 25;

    @Config.Name("Frostborn Draugr Attack Damage")
    @Config.Comment("Change the Attack Daamge of the Frostborn Draugr")
    @Config.RequiresMcRestart
    public static double draugr_attack_damage = 16;

    @Config.Name("Wyrk Health")
    @Config.Comment("Change the Health of the Mini Wyrks")
    @Config.RequiresMcRestart
    public static double wyrk_health = 35;

    @Config.Name("Wyrk Attack Damage")
    @Config.Comment("Change the Attack damage of the Mini Wyrks")
    @Config.RequiresMcRestart
    public static double wyrk_attack_damage = 14;

    @Config.Name("Friendly Wyrk Heal Amount")
    @Config.Comment("When the friendly Wyrk collects souls, it will heal itself this amount when it's health reaches a 95% threshold")
    @Config.RequiresMcRestart
    public static float wyrk_heal_amount = 5;

    @Config.Name("Wyrk Starter Souls")
    @Config.Comment("Change the amount of souls the Mini Wyrk can start with for summoning Draugr, take in the mind the system is set up so that every Draugr that dies nearby, the Wyrk can re-summon them. Default : 0")
    @Config.RequiresMcRestart
    public static int wyrk_starter_souls = 0;

    @Config.Name("Frostborn Draugr Champion Health")
    @Config.Comment("Change the health of the Frostborn Draugr Champion")
    @Config.RequiresMcRestart
    public static double champion_health = 175;

    @Config.Name("Frostborn Draugr Champion Attack Damage")
    @Config.Comment("Change the attack damage of the Frostborn Draugr Champion")
    @Config.RequiresMcRestart
    public static double champion_attack_damage = 22;

    @Config.Name("Frostborn Draugr Champion Armor")
    @Config.Comment("Change the armor value of the Frostborn Draugr Champion")
    @Config.RequiresMcRestart
    public static double champion_armor = 18;

    @Config.Name("Frostborn Draugr Champion Armor Toughness")
    @Config.Comment("Change the armor toughness value of the Frostborn Draugr Champion")
    @Config.RequiresMcRestart
    public static double champion_armor_toughness = 4;

    @Config.Name("Frostborn Draugr Champion Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Frostborn Draugr Champion. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float champion_damage_cap = 25;

    @Config.Name("Ancient Wyrk Health")
    @Config.Comment("Change the health of the Ancient Wyrk")
    @Config.RequiresMcRestart
    public static double great_wyrk_health = 270;

    @Config.Name("Ancient Wyrk Attack Damage")
    @Config.Comment("Change the attack damage of the Ancient Wyrk")
    @Config.RequiresMcRestart
    public static double great_wyrk_attack_damage = 30;

    @Config.Name("Ancient Wyrk Armor")
    @Config.Comment("Change the armor value of the Ancient Wyrk")
    @Config.RequiresMcRestart
    public static double great_wyrk_armor = 18;

    @Config.Name("Ancient Wyrk Armor Toughness")
    @Config.Comment("Change the armor toughness of the Ancient Wyrk")
    @Config.RequiresMcRestart
    public static double great_wyrk_armor_toughness = 7;

    @Config.Name("Ancient Wyrk Cooldown")
    @Config.Comment("Change the cooldown of the Ancient Wyrk, in seconds")
    @Config.RequiresMcRestart
    public static int great_wyrk_cooldown = 5;

    @Config.Name("Ancient Wyrk Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Ancient Wyrk. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float great_wyrk_damage_cap = 30;

    @Config.Name("Cult of Roh Assassin Health")
    @Config.Comment("Change the health of the Cult of Roh Assassin")
    @Config.RequiresMcRestart
    public static double assassin_healht = 35;

    @Config.Name("Cult of Roh Assassin Attack Damage")
    @Config.Comment("Change the attack damage of the Cult of Roh Assassin")
    @Config.RequiresMcRestart
    public static double assassin_attack_damage = 16;

    @Config.Name("Cult of Roh Assassin Armor")
    @Config.Comment("Change the armor value of the Cult of Roh Assassin")
    @Config.RequiresMcRestart
    public static double assassin_armor = 4;

    @Config.Name("Cult of Roh Mobs Disappears in Daylight")
    @Config.Comment("Change if the Cult of Roh mobs disappears when its daylight and can see the sun. default: true")
    @Config.RequiresMcRestart
    public static boolean assassin_burns_daylight = true;

    @Config.Name("Cult of Roh Assassin Undead Enable/Disable")
    @Config.Comment("Change if the Cult of Roh Assassin classifies as undead or not. Default: true")
    @Config.RequiresMcRestart
    public static boolean cult_assassin_status = true;

    @Config.Name("Cult of Roh Sorcerer Undead Enable/Disable")
    @Config.Comment("Change if the Cult of Roh Sorcerer classifies as undead or not. Default: true")
    @Config.RequiresMcRestart
    public static boolean cult_sorcerer_status = true;

    @Config.Name("Cult of Roh Crypt Guard Undead Enable/Disable")
    @Config.Comment("Change if the Cult of Roh Assassin classifies as undead or not. Default: true")
    @Config.RequiresMcRestart
    public static boolean cult_crypt_guard_status = true;

    @Config.Name("Cult of Roh Sorcerer Attack Damage")
    @Config.Comment("Change the attack damage of the Cult of Roh Sorcerer")
    @Config.RequiresMcRestart
    public static double sorcerer_attack_damage = 20;

    @Config.Name("Cult of Roh Sorcerer Health")
    @Config.Comment("Change the health of the Cult of Roh Sorcerer")
    @Config.RequiresMcRestart
    public static double sorcerer_health = 65;

    @Config.Name("Cult of Roh Sorcerer Armor")
    @Config.Comment("Change the Armor value of the Cult of Roh Sorcerer")
    @Config.RequiresMcRestart
    public static double sorcerer_armor = 8;

    @Config.Name("Cult of Roh Sorcerer Minions Cap")
    @Config.Comment("Change how many minions the Cult of Roh Sorcerer can have active around it")
    @Config.RequiresMcRestart
    public static int sorcerer_minions_cap = 3;

    @Config.Name("Cult of Roh SorcererMob Spawn List")
    @Config.Comment("Add or remove possible mob spawns that the Cult of Roh Sorcerer can summon. Must be ModID:entity_name")
    @Config.RequiresMcRestart
    public static String[] sorcerer_spawn_list = {
            "da:dark_assassin", "da:dark_royal"
    };

    @Config.Name("Cult of Roh Crypt Guard Health")
    @Config.Comment("Change the health of the Roh Crypt Guard")
    @Config.RequiresMcRestart
    public static double crypt_guard_health = 50;

    @Config.Name("Cult of Roh Crypt Guard Attack Damage")
    @Config.Comment("Change the attack damage of the Roh Crypt Guard")
    @Config.RequiresMcRestart
    public static double crypt_guard_attack_damage = 22;

    @Config.Name("Cult of Roh Crypt Guard Armor")
    @Config.Comment("Change the armor value of the Roh Crypt Guard")
    @Config.RequiresMcRestart
    public static double crypt_guard_armor = 18;

    @Config.Name("Cult of Roh Crypt Guard Shield Chance")
    @Config.Comment("Change the chance that a Crypt Guard will spawn with a shield, higher means less likely. default: 7")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 10)
    public static int crypt_guard_shield_chance = 7;

    @Config.Name("Cult of Roh Crypt Guard Potion Chance")
    @Config.Comment("Change the chance that a Crypt Guard will spawn with a Lingering Potion. Only ones without shields can equip these. Higher means less likely. default: 8")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 10)
    public static int crypt_guard_potion_chance = 8;

    @Config.Name("High Court Knights Health")
    @Config.Comment("Change the health of the High Court Knights")
    @Config.RequiresMcRestart
    public static double hcc_knight_health = 95;

    @Config.Name("High Court Knights Attack Damage")
    @Config.Comment("Change the attack damage of the High Court Knights")
    @Config.RequiresMcRestart
    public static double hcc_knight_attack_damage = 30;

    @Config.Name("High Court Knights Armor")
    @Config.Comment("Change the armor value of the High Court Knights")
    @Config.RequiresMcRestart
    public static double hcc_knight_armor = 16;

    @Config.Name("Farum Elder Gargoyle Health")
    @Config.Comment("Change the health of the Farum Elder Gargoyle")
    @Config.RequiresMcRestart
    public static double elder_gargoyle_health = 35;

    @Config.Name("Farum Elder Gargoyle Attack Damage")
    @Config.Comment("Change the attack damage of the Farum Elder Gargoyle")
    @Config.RequiresMcRestart
    public static double elder_gargoyle_damage = 18;

    @Config.Name("Farum Elder Gargoyle Armor")
    @Config.Comment("Change the armor value of the Farum Elder Gargoyle")
    @Config.RequiresMcRestart
    public static double elder_gargoyle_armor = 7;

    @Config.Name("Farum Gargoyle Health")
    @Config.Comment("Change the health of the Farum Gargoyle")
    @Config.RequiresMcRestart
    public static double gargoyle_health = 40;

    @Config.Name("Farum Gargoyle Attack Damage")
    @Config.Comment("Change the attack damage of the Farum Gargoyle")
    @Config.RequiresMcRestart
    public static double gargoyle_damage = 23;

    @Config.Name("Farum Gargoyle Armor")
    @Config.Comment("Change the armor value of the Farum Gargoyle")
    @Config.RequiresMcRestart
    public static double gargoyle_armor = 10;

    @Config.Name("High King of the Sky Dragon Health")
    @Config.Comment("Change the health of the High King of the Sky Dragon")
    @Config.RequiresMcRestart
    public static double high_dragon_health = 460;

    @Config.Name("High King/Dragon Attack Damage")
    @Config.Comment("Change the attack damage of the High King/Dragon")
    @Config.RequiresMcRestart
    public static double high_dragon_king_damage = 32;

    @Config.Name("High King of the Sky Dragon Armor")
    @Config.Comment("Change the armor value of the High King of the Sky Dragon")
    @Config.RequiresMcRestart
    public static double high_dragon_armor = 12;

    @Config.Name("High King of the Sky Dragon Armor Toughness")
    @Config.Comment("Change the armor toughness value of the High King of the Sky Dragon")
    @Config.RequiresMcRestart
    public static double high_dragon_armor_toughness = 2;

    @Config.Name("High King of The Sky Dragon Fly Speed")
    @Config.Comment("Change the fly speed of the Dragon while it is NOT doing an attack")
    @Config.RequiresMcRestart
    public static double high_dragon_fly_speed = 0.2;

    @Config.Name("High King of the Sky Dragon Fly Speed Attacking")
    @Config.Comment("Change the fly speed of the Dragon while it is doing an attack")
    @Config.RequiresMcRestart
    public static double high_dragon_attack_fly_speed = 0.15;

    @Config.Name("High King of the Sky Dragon Air Time")
    @Config.Comment("Change the static of how much time the dragon must be in the air before going to the ground, In Seconds")
    @Config.RequiresMcRestart
    public static double high_dragon_fly_time = 45;

    @Config.Name("High King of the Sky Dragon Ground Time")
    @Config.Comment("Change the static of how much time the dragon must be on ground before going back into the air, In Seconds")
    @Config.RequiresMcRestart
    public static double high_dragon_ground_time = 32.5;

    @Config.Name("High King/Dragon Divine Ring Damage Modifier")
    @Config.Comment("This attack disregards armor when the player fails to avoid the divine rings, it will take X health away from the player as a percentage of there max health")
    @Config.RequiresMcRestart
    public static double divine_ring_damage_percentage = 0.25;

    @Config.Name("High King/Dragon Divine Ring Lifesteal Modifier")
    @Config.Comment("How much Percentage of health does the boss heal if the player fails to make this attack.")
    @Config.RequiresMcRestart
    public static double divine_ring_life_steal = 0.05;

    @Config.Name("High King of the Sky Dragon onDeath")
    @Config.Comment("Change if you want that when the High King of the Sky Dragon dies, does it summon the High King for the second part of the boss fight. Default : true")
    @Config.RequiresMcRestart
    public static boolean high_dragon_after_death = true;

    @Config.Name("High King Boss Switch")
    @Config.Comment("When set to false, instead of the Dragon spawning, it will only spawn the High King. Useful for Modpack makers that wanna put the dragon boss somewhere else")
    @Config.RequiresMcRestart
    public static boolean dragon_starts_first = true;

    @Config.Name("High King of the Sky Dragon Experience")
    @Config.Comment("If you changed it to when the dragon dies, it doesn't spawn The High King, How Much Experience does it drop?")
    @Config.RequiresMcRestart
    public static int high_dragon_experience_value = 260;

    @Config.Name("High King of the Sky Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the High King of the Sky. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float high_dragon_damage_cap = 35;

    @Config.Name("The High King Health")
    @Config.Comment("Change the health of the High King")
    @Config.RequiresMcRestart
    public static double high_king_health = 440;

    @Config.Name("The High King Armor")
    @Config.Comment("Change the armor value of the High King")
    @Config.RequiresMcRestart
    public static double high_king_armor = 18;

    @Config.Name("The High King Experience Value")
    @Config.Comment("Change how much experience the High King drops upon Death")
    @Config.RequiresMcRestart
    public static int high_king_experience_value = 500;

    @Config.Name("The High King Armor Toughness")
    @Config.Comment("Change the armor toughness value of the High King")
    @Config.RequiresMcRestart
    public static double high_king_armor_toughness = 8;

    @Config.Name("The High King Block Cooldown")
    @Config.Comment("Change how long the block cooldown is in between each time the boss can block an attack, in Seconds")
    @Config.RequiresMcRestart
    public static int high_king_block_cooldown = 4;

    @Config.Name("The High King Aoe Cooldown")
    @Config.Comment("Change how long before the High King can iniated another AOE attack, this is used to prevent spam of this attack, in Seconds")
    @Config.RequiresMcRestart
    public static int high_king_aoe_cooldown = 15;

    @Config.Name("The High King General Cooldown Min")
    @Config.Comment("Change how long before the High King can do another attack, on the lowest random value, in Seconds")
    @Config.RequiresMcRestart
    public static int high_king_cooldown_min = 1;

    @Config.Name("The High King General Cooldown Max")
    @Config.Comment("Change how long before the High King can do another attack, on the high random value, in Seconds. VALUE MUST BE GREATER THAN MIN")
    @Config.RequiresMcRestart
    public static int high_king_cooldown_max = 2;

    @Config.Name("The High King Thrust Lifesteal factor")
    @Config.Comment("Change how much Health the High King heals when doing a successful Thrust attack with his Spear, this is in a percentage. Default : 0.05 or 5%")
    @Config.RequiresMcRestart
    public static double high_king_lifesteal_thrust = 0.05;

    @Config.Name("The High King Grab Lifesteal factor")
    @Config.Comment("Change how much Health the High King heals when doing a successful Grab attack with his claw hand, this is in a percentage. Default : 0.07 or 7%")
    @Config.RequiresMcRestart
    public static double high_king_lifesteal_grab = 0.07;

    @Config.Name("The High King Blood Phase")
    @Config.Comment("When set to true, this will disable all blood related attacks from the High King. This will still keep the Phase Transition, it will just cut out the Phrases")
    @Config.RequiresMcRestart
    public static boolean disable_blood_attacks = false;

    @Config.Name("The High King Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the High King. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float high_king_damage_cap = 34;

    @Config.Name("Voidiant Health")
    @Config.Comment("Change the health of the Voidiant.")
    @Config.RequiresMcRestart
    public static double voidiant_health = 60;

    @Config.Name("Voidiant Attack Damage")
    @Config.Comment("Change the attack damage of the Voidiant")
    @Config.RequiresMcRestart
    public static double voidiant_attack_damage = 20;

    @Config.Name("Voidiant Armor")
    @Config.Comment("Change the armor of the Voidiant")
    @Config.RequiresMcRestart
    public static double voidiant_armor=8;

    @Config.Name("Aegyptia Recurian Health")
    @Config.Comment("Change the health of the Aegyptia Recurian.")
    @Config.RequiresMcRestart
    public static double aegyptia_health = 38;

    @Config.Name("Aegyptia Recurian Attack Damage")
    @Config.Comment("Change the attack damage of the Aegyptia Recurian")
    @Config.RequiresMcRestart
    public static double aegyptia_attack_damage = 16;

    @Config.Name("Aegyptia Recurian Armor")
    @Config.Comment("Change the armor of the Aegyptia Recurian")
    @Config.RequiresMcRestart
    public static double aegyptia_armor = 12;

    @Config.Name("Everator Health")
    @Config.Comment("Change the health of the Everator.")
    @Config.RequiresMcRestart
    public static double everator_health = 200;

    @Config.Name("Everator Attack Damage")
    @Config.Comment("Change the attack damage of the Everator")
    @Config.RequiresMcRestart
    public static double everator_attack_damage = 19;

    @Config.Name("Everator Armor")
    @Config.Comment("Change the armor of the Everator")
    @Config.RequiresMcRestart
    public static double everator_armor = 18;

    @Config.Name("Everator Armor Toughness")
    @Config.Comment("Change the armor toughness of the Everator")
    @Config.RequiresMcRestart
    public static double everator_armor_toughness = 6;

    @Config.Name("Everator Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Everator. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float everator_damage_cap = 25;

    @Config.Name("Scutter Beetle Health")
    @Config.Comment("Change the health of the Scutter Beetle.")
    @Config.RequiresMcRestart
    public static double scutter_beetle_health = 14;

    @Config.Name("Scutter Beetle Attack Damage")
    @Config.Comment("Change the attack damage of the Scutter Beetle")
    @Config.RequiresMcRestart
    public static double scutter_beetle_damage = 13;

    @Config.Name("Scutter Beetle Armor")
    @Config.Comment("Change the armor of the Scutter Beetle")
    @Config.RequiresMcRestart
    public static double scutter_beetle_armor = 20;

    @Config.Name("Cursed Revenant Health")
    @Config.Comment("Change the health of the Cursed Revenant.")
    @Config.RequiresMcRestart
    public static double reanimate_health = 25;

    @Config.Name("Cursed Revenant Attack Damage")
    @Config.Comment("Change the attack damage of the Cursed Revenant")
    @Config.RequiresMcRestart
    public static double reanimate_damage = 10;


    @Config.Name("Cursed Sentinel Armor")
    @Config.Comment("Change the armor of the Cursed Sentinel")
    @Config.RequiresMcRestart
    public static double cursed_sentinel_armor = 12;

    @Config.Name("Cursed Sentinel Health")
    @Config.Comment("Change the health of the Cursed Sentinel.")
    @Config.RequiresMcRestart
    public static double cursed_sentinel_health = 75;

    @Config.Name("Cursed Sentinel Attack Damage")
    @Config.Comment("Change the attack damage of the Cursed Sentinel")
    @Config.RequiresMcRestart
    public static double cursed_sentinel_damage = 14;

    @Config.Name("Cursed Sentinel Damage Source Pickaxes")
    @Config.Comment("Change how much damage the Cursed Sentinel is multiplied when damaged from a pickaxe")
    @Config.RequiresMcRestart
    public static double cursed_sentinel_pickaxe_multiplier = 1.5;

    @Config.Name("Cursed Sentinel Damage Source Regular")
    @Config.Comment("Change how much damage the Cursed Sentinel is multiplied when damaged from any melee source.")
    @Config.RequiresMcRestart
    public static double cursed_sentinel_regular_multiplier = 0.25;

    @Config.Name("Cursed Revenant Armor")
    @Config.Comment("Change the armor of the Cursed Revenant")
    @Config.RequiresMcRestart
    public static double reanimate_armor = 12;

    @Config.Name("Apathyr Health")
    @Config.Comment("Change the health of the Apathyr.")
    @Config.RequiresMcRestart
    public static double apathyr_health = 430;

    @Config.Name("Apathyr Attack Damage")
    @Config.Comment("Change the attack damage of the Apathyr")
    @Config.RequiresMcRestart
    public static double apathyr_damage = 26;

    @Config.Name("Apathyr Armor")
    @Config.Comment("Change the armor of the Apathyr")
    @Config.RequiresMcRestart
    public static double apathyr_armor = 20;

    @Config.Name("Apathyr Armor Toughness")
    @Config.Comment("Change the armor tougness of the Apathyr")
    @Config.RequiresMcRestart
    public static double apathyr_armor_toughness = 8;

    @Config.Name("Apathyr Damage Cap")
    @Config.Comment("Change the damage cap of how much damage can be done at one time to the Apathyr. Disable this feature entirely in general_config")
    @Config.RequiresMcRestart
    public static float apathyr_damage_cap = 28;

    @Config.Name("Apathyr Cooldown Base")
    @Config.Comment("Change the cooldown of the Apathyr's attack at the base level. In seconds")
    @Config.RequiresMcRestart
    public static double apathyr_cooldown = 1.5;

    @Config.Name("Apathyr Experience Value")
    @Config.Comment("Change how much experience the Apathyr drops upon Death")
    @Config.RequiresMcRestart
    public static int apathyr_experience_value = 300;

    @Config.Name("Apathyr Life steal Amount")
    @Config.Comment("Change the percentage of health healed to the Apathyr when it gets a successful grab attack on the target")
    @Config.RequiresMcRestart
    public static double apathyr_life_steal_amount = 0.07;

    @Config.Name("Apathyr Disable Solo Mode")
    @Config.Comment("When enabled, you can fight the Apathyr with others. When disabled, this boss can only be fought solo.")
    @Config.RequiresMcRestart
    public static boolean apathyr_disable_solo_mode = false;

}
