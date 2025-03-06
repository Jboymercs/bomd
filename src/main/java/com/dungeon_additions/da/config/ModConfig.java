package com.dungeon_additions.da.config;


import com.dungeon_additions.da.util.ModReference;
import net.minecraft.init.Biomes;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/general_config")
public class ModConfig {
    @Config.Name("Boss Scaling Health")
    @Config.Comment("Change the scaling added per player of health added to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_health_scaling = 0.4;

    @Config.Name("Boss Scaling Attack Damage")
    @Config.Comment("Change the scaling added per player of attack damage to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_attack_damage_scaling = 0.18;

    @Config.Name("Poison Dart Damage")
    @Config.Comment("Change the damage done by Poison Darts, used by Mini Blossoms and the Void Dagger")
    @Config.RequiresMcRestart
    public static double poison_dart_damage = 7;

    @Config.Name("Void Dagger Cooldown")
    @Config.Comment("Change the cool down of the Void Dagger")
    @Config.RequiresMcRestart
    public static int dagger_cooldown = 7;

    @Config.Name("Greatsword of Ambition Ability Cooldown")
    @Config.Comment("Change the cooldown time for the Sword Of Ambition in seconds")
    @Config.RequiresMcRestart
    public static int sword_of_ambition_cooldown = 12;

    @Config.Name("Greatsword Of Ambition Damage")
    @Config.Comment("Change the damage factor")
    @Config.RequiresMcRestart
    public static float sword_of_ambition_damage = 9;

    @Config.Name("Soul Spear Damage")
    @Config.Comment("Change the damage of the Soul Spear")
    @Config.RequiresMcRestart
    public static float soul_spear_damage = 6;

    @Config.Name("Soul Spear RED Damange")
    @Config.Comment("Change the damage of the Soul Spear RED")
    public static float soul_weapon_damage = 7;

    @Config.Name("Soul Spear Ability Cooldown")
    @Config.Comment("Change the cooldown of the Soul Spears ability in seconds Both types!")
    @Config.RequiresMcRestart
    public static int soul_spear_cooldown = 10;

    @Config.Name("Void Dagger Damage")
    @Config.Comment("Change the damage factor of the Void Dagger")
    @Config.RequiresMcRestart
    public static double void_dagger_damage = 5;

    @Config.Name("Stormvier Rapier Damage")
    @Config.Comment("Change the damage factor of the Stormvier Rapier")
    @Config.RequiresMcRestart
    public static double rapier_damage = 5.5;

    @Config.Name("Frostborn Sword Damage")
    @Config.Comment("Change the damage factor of the Frostborn Sword")
    @Config.RequiresMcRestart
    public static double frost_sword_damage = 6.5;

    @Config.Name("Frostborn Champion Axe Damage")
    @Config.Comment("Change the damage factor of the Frostborn Champion Axe")
    @Config.RequiresMcRestart
    public static double champion_axe_damage = 9;

    @Config.Name("Frostborn Armor Ability Chance")
    @Config.Comment("Change the chance of the Strength being given with the Frostborn's armor, higher means better chance at higher health")
    @Config.RequiresMcRestart
    public static int champion_armor_chance = 7;

    @Config.Name("Ancient Wyrk Armor Ability Chance")
    @Config.Comment("Change the chance of the Regeneration being given with the Ancient Wyrks armor, higher means better chance at higher health")
    @Config.RequiresMcRestart
    public static int wyrk_armor_chance = 7;

    @Config.Name("Litic Armor Ability Chance")
    @Config.Comment("Change the chance of the Regeneration being given with the Litic armor, higher means better chance at higher health")
    @Config.RequiresMcRestart
    public static int litic_armor_chance = 7;

    @Config.Name("Ancient Wyrk Staff Cooldown")
    @Config.Comment("Change the staff cooldown for the frost bullet attack, this scales the alternative attack additionally")
    @Config.RequiresMcRestart
    public static int wyrk_staff_cooldown = 3;

    @Config.Name("Ancient Wyrk Staff Frost Bullet damage")
    @Config.Comment("Change the Ancient Wyrk staff frost bullet damage")
    @Config.RequiresMcRestart
    public static float wyrk_staff_bullet_damage = 6;

    @Config.Name("Ancient Wyrk Staff Lazer Damage")
    @Config.Comment("change the Ancient Wyrk staff's max lazer damage, this scales with how long the staff has been charged")
    @Config.RequiresMcRestart
    public static int wyrk_staff_lazer_damage = 18;

    @Config.Name("Frostborn Champion Axe Chance")
    @Config.Comment("Change the damage scaling the champion axe can do. Higher the number, the more damage it does the lower the health of the player")
    @Config.RequiresMcRestart
    public static float champion_axe_damage_scaling = 4;

    @Config.Name("Crafting Material ToolTip")
    @Config.Comment("Should Items listed as Crafting Material state that is a crafting material. False will disable the tool tip")
    @Config.RequiresMcRestart
    public static boolean enable_crafting_tooltips = true;

    @Config.Name("Soul Star Block Deletion")
    @Config.Comment("Do Soul Star Altar blocks disappear when all are activated, set to false to disable")
    @Config.RequiresMcRestart
    public static boolean soul_star_blocks_remove = true;

    @Config.Name("Soul Star Drop Chance")
    @Config.Comment("Change the drop chance of Soul Star from anything that extends off of EntityMob, Like Zombies, Skeletons, and so on")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 100)
    public static int soul_star_drop_chance = 1;

    @Config.Name("Soul Star Drops everywhere")
    @Config.Comment("Change if the Soul Star should drop from anything that extends from EntityMob, setting to true will enable this setting")
    @Config.RequiresMcRestart
    public static boolean soul_star_drops_everywhere = false;

    @Config.Name("Structure Location Resetting")
    @Config.Comment("At what radius must any structure locating item will reset and find another one. Like getting close to a current Lich Tower in x blocks, will reset the item and find another one")
    @Config.RequiresMcRestart
    public static int locator_reset_pos = 60;

    @Config.Name("Night Lich Tower Search Radius")
    @Config.Comment("Change how far the Soul Star and Locate command searches for a Night Lich's Tower. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int lich_search_distance = 100;

    @Config.Name("Void Blossom Cave Search Radius")
    @Config.Comment("Change how far the Void Lily and Locate command searches for a Void Blossoms Cave. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int void_blosom_search_distance = 100;

    @Config.Name("Frozen Castle Search Radius")
    @Config.Comment("Change how far the Frozen Soul Star and Locate command searches for a Frozen Castle. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int frozen_castle_search_distance = 175;

    @Config.Name("High Court City Search Radius")
    @Config.Comment("Change how far the locator item and command searches for the High Court City. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int high_court_city_search_distance = 175;

    @Config.Name("Soul Star Drops Advancement Requirements")
    @Config.Comment("What advancements are required for players to use and unlock Soul Stars dropping from Mobs")
    @Config.RequiresMcRestart
    public static String[] soul_star_progress = {
            "da:kill_kobf"
    };

    @Config.Name("Cult of Rah Assassins Spawning Advancement Requirements")
    @Config.Comment("What advancements are required for Cult of Rah Assassins spawning around the player naturally at night time")
    @Config.RequiresMcRestart
    public static String[] assassins_spawn_progress = {
            "da:kill_great_wyrk"
    };

    @Config.Name("Cult of Rah Assassin Spawn Rate")
    @Config.Comment("Change the spawn rate of Cult of Rah Assassins, change to 0 to disable")
    @Config.RequiresMcRestart
    public static int assassin_spawn_rate = 8;

    @Config.Name("Boss Reset Enabled/Disabled")
    @Config.Comment("This setting makes it so that if bosses killed all players around them, they will reset back into a key block with a chest above with the respective key. Allowing players to recover there loot more easily and try again. default: true")
    @Config.RequiresMcRestart
    public static boolean boss_reset_enabled = true;

    @Config.Name("Boss Reset Timer")
    @Config.Comment("A timer before boss reset occurs in seconds, basically if any players are not within the bosses follow radius (sight is not needed) and the boss has engaged any player in the past. This timer will countdown till reset")
    @Config.RequiresMcRestart
    public static int boss_reset_timer = 25;

    @Config.Name("Soul Star Advancement Requirement Enable/Disable")
    @Config.Comment("Change to false to disable advancement requirements for Soul Stars to drop")
    @Config.RequiresMcRestart
    public static boolean advancements_block_soul_stars = true;
}
