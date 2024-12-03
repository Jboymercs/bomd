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

    @Config.Name("Void Blossom New Attacks Enable/Disalbe")
    @Config.Comment("For those that don't want minions to spawn during the fight, when set to false minions will not spawn from the void blossom")
    @Config.RequiresMcRestart
    public static boolean enable_minions = true;

    @Config.Name("Void Blossom Attack Damage")
    @Config.Comment("Change the Attack Damage of the Void Blossom")
    @Config.RequiresMcRestart
    public static double blossom_attack_damange = 12;

    @Config.Name("Void Blossom Spikes Lag Reducer")
    @Config.Comment("When enabled, the Void spikes will only spawn within 20 blocks of all players, to help reduce lag when being spawned")
    @Config.RequiresMcRestart
    public static boolean spike_lag_reducer = false;

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
    public static double abberrant_damage = 8;

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
    public static double burning_knight_damage = 18;

    @Config.Name("Knight of Burning Flame Health")
    @Config.Comment("Change the health of this boss (Knight of Burning Flame)")
    @Config.RequiresMcRestart
    public static double burning_knight_health = 300;

    @Config.Name("Knight of Burning Flame Block Cooldown")
    @Config.Comment("Change the timer that the Knight of Burning Flame can block attacks, take not this is only fired between cool downs of each attack in seconds")
    @Config.RequiresMcRestart
    public static double burning_knight_block_cooldown = 3.5;

    @Config.Name("Knight of Burning Flame Health Potion Amount")
    @Config.Comment("Change the amount of health healed when the Knight of Burning Flame uses a health potion. this is measured in X / Maxhealth. so .07 would be 7% of it's max health")
    @Config.RequiresMcRestart
    public static double burning_knight_heal_amount = 0.07;

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
    public static int kobf_experience_orb_value = 8;

    @Config.Name("Knight of Burning Flame AOE Damage")
    @Config.Comment("How much damage is dealt by the magma blocks popping out of the ground, note this takes affect to the sword as well")
    @Config.RequiresMcRestart
    public static float aoe_block_damage = 10;

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

}
