package com.dungeon_additions.da.config;


import com.dungeon_additions.da.util.ModReference;
import net.minecraft.init.Biomes;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/general_config")
public class ModConfig {

    @Config.Name("Aether Content Compat")
    @Config.Comment("If Aether Continuation, Aether Legacy, and Aether Lost Content are istalled, will they change the High Court City structure")
    @Config.RequiresMcRestart
    public static boolean aether_compat = true;

    @Config.Name("Experimental Features Enabled/Disabled")
    @Config.Comment("Change if experimental features are enabled. this mostly includes boss music. default: false")
    @Config.RequiresMcRestart
    public static boolean experimental_features = true;

    @Config.Name("Boss Scaling Health")
    @Config.Comment("Change the scaling added per player of health added to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_health_scaling = 0.4;

    @Config.Name("Boss Scaling Attack Damage")
    @Config.Comment("Change the scaling added per player of attack damage to bosses, not counting one player")
    @Config.RequiresMcRestart
    public static double boss_attack_damage_scaling = 0.18;

    @Config.Name("Boss Damage Cap Enable/Disalbe")
    @Config.Comment("When enabled, bosses have damage caps that players can do to them. They are typically quite high but more for exploits in modded settings")
    @Config.RequiresMcRestart
    public static boolean boss_cap_damage_enabled = true;

    @Config.Name("Armor Scaling")
    @Config.Comment("Scale the armor values of all of the armor items in the mod")
    @Config.RequiresMcRestart
    public static double armor_scaling = 1.0;

    @Config.Name("Armor Toughness Scaling")
    @Config.Comment("Scale the armor toughness values of all the armor items in the mod")
    @Config.RequiresMcRestart
    public static double armor_toughness_scaling = 1.0;

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

    @Config.Name("Incendium Helmet Sword Boost Multiplier")
    @Config.Comment("Change the multiplier of how much damage or effects are added when using a swords ability and weaing the Incendium Helmet")
    @Config.RequiresMcRestart
    public static double incendium_helmet_multipler = 1.5;

    @Config.Name("Ambitious Flame Blade Damage")
    @Config.Comment("Change the damage of the Ambitious Flame Blade")
    @Config.RequiresMcRestart
    public static float flame_blade_damage = 6;

    @Config.Name("Ambitious Flame Blade Cooldown")
    @Config.Comment("Change the cooldown of the Ambitious Flame Blade in seconds")
    @Config.RequiresMcRestart
    public static int flame_blade_cooldown = 14;

    @Config.Name("Sealed Bottle Cooldown")
    @Config.Comment("Change the cooldown of all Sealed Bottles")
    @Config.RequiresMcRestart
    public static int sealed_bottle_cooldown = 8;

    @Config.Name("Incendium Shield Cooldown")
    @Config.Comment("Change the cooldown of the Incendium Shield in seconds")
    @Config.RequiresMcRestart
    public static int incendium_shield_cooldown = 12;

    @Config.Name("Incendium Shield Damage")
    @Config.Comment("Change the damage that the Incendium Shields Fire ability does to enemies")
    @Config.RequiresMcRestart
    public static float incendium_shield_damage = 6;

    @Config.Name("Frostborn Shield Cooldown")
    @Config.Comment("Change the cooldown of the Frostborn Shield ability in seconds.")
    @Config.RequiresMcRestart
    public static int frostborn_shield_cooldown = 10;

    @Config.Name("Frostborn Shield Damage")
    @Config.Comment("Change the damage of the Frostborn Shield when doing a ram attack")
    @Config.RequiresMcRestart
    public static float frostborn_shield_damage = 6;

    @Config.Name("Dark Metal Shield Cooldown")
    @Config.Comment("Change the cooldown of the Dark Metal Shield ability in seconds")
    @Config.RequiresMcRestart
    public static int dark_shield_cooldown = 14;

    @Config.Name("Obsidilith Shield Cooldown")
    @Config.Comment("Change the cooldown of the Obsidilith Shield ability in seconds")
    @Config.RequiresMcRestart
    public static int obsidilith_shield_ability = 17;

    @Config.Name("Obsidilith Shield Damage")
    @Config.Comment("Change the damage of the Obsidilith's Shield ability")
    @Config.RequiresMcRestart
    public static float obsidilith_shield_damage = 10;

    @Config.Name("Dark Metal Shield Damage")
    @Config.Comment("Change the damage of the Dark Metal Shiels when doing an AOE attack")
    @Config.RequiresMcRestart
    public static float dark_shield_damage = 9;

    @Config.Name("Dark Metal Full Armor Set Bonus")
    @Config.Comment("Change how much shield damage is boosted when having the full Dark Metal Armor set equipped")
    @Config.RequiresMcRestart
    public static double dark_armor_multiplier = 1.8;

    @Config.Name("Dark Metal Sicle Cooldown")
    @Config.Comment("Change the cooldown of the Dark Metal Sicle ability, in seconds")
    @Config.RequiresMcRestart
    public static int dark_sicle_cooldown = 10;

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

    @Config.Name("Flame Metal Fire Resistance")
    @Config.Comment("Change if the Flame Metal Set should give Permanent Fire Resistance when all pieces are on.")
    @Config.RequiresMcRestart
    public static boolean flame_metal_fire_resistance = true;

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
    public static float wyrk_staff_bullet_damage = 8;

    @Config.Name("Ancient Wyrk Staff Lazer Damage")
    @Config.Comment("change the Ancient Wyrk staff's max lazer damage, this scales with how long the staff has been charged")
    @Config.RequiresMcRestart
    public static int wyrk_staff_lazer_damage = 18;

    @Config.Name("Frostborn Champion Axe Chance")
    @Config.Comment("Change the damage scaling the champion axe can do. Higher the number, the more damage it does the lower the health of the player")
    @Config.RequiresMcRestart
    public static float champion_axe_damage_scaling = 1;

    @Config.Name("Bloodied Sword Spear Cooldown")
    @Config.Comment("Change the cooldown in seconds for the Bloodied Swordspears Bloody Sprat ability, The Bloody Dash is always double the amount of time of this.")
    @Config.RequiresMcRestart
    public static int bloody_sword_spear_cooldown = 7;

    @Config.Name("Bloodied Sword Spear Damage")
    @Config.Comment("Change the Damage of the Bloodied Sword Spear")
    @Config.RequiresMcRestart
    public static double bloodied_sword_spear_damage = 10;

    @Config.Name("Divine Sword Spear Cooldown")
    @Config.Comment("Change the cooldown in seconds for the Cast Light Ring Ability from the Divine Sword Spear")
    @Config.RequiresMcRestart
    public static int divine_sword_spear_cooldown_1 = 8;

    @Config.Name("Divine Sword Spear Cooldown 2")
    @Config.Comment("Change the cooldown in seconds for the AOE ability from the Divine Sword Spear in seconds")
    @Config.RequiresMcRestart
    public static int divine_sword_spear_cooldown_2 = 22;

    @Config.Name("Divine Sword Spear Damage")
    @Config.Comment("Change the Damage of the Divine Sword Spear")
    @Config.RequiresMcRestart
    public static double divine_sword_spear_damage = 9.5;

    @Config.Name("The Prosperous Assault Cooldown")
    @Config.Comment("Change the cooldown of the Prosperous Assault in seconds")
    @Config.RequiresMcRestart
    public static int prosperous_assault_cooldown = 15;

    @Config.Name("Prosperous Assault Damage")
    @Config.Comment("Change the Damage of the Prosperous Assault")
    @Config.RequiresMcRestart
    public static double prosperous_assault_damage = 9.5;

    @Config.Name("Dragon Scourage Bow Cooldown")
    @Config.Comment("Change the cooldown in seconds before being able to use the Dragon Scourage Bow.")
    @Config.RequiresMcRestart
    public static int dragon_bow_cooldown = 25;

    @Config.Name("Dragon Scourage Bow Base Damage")
    @Config.Comment("Change the base damage of the Dragon Scourage Bow, take note that bows do damage weirdly. Even if at 3, it'll do 15-17 damage per full charged shot.")
    @Config.RequiresMcRestart
    public static float dragon_bow_damage = 3;

    @Config.Name("King's Claw Cooldown")
    @Config.Comment("Change the cooldown of the King's Claw, take note that when having an offhand claw, this cooldown is halved.")
    @Config.RequiresMcRestart
    public static int king_claw_cooldown = 20;

    @Config.Name("King's Claw Damage")
    @Config.Comment("Change the Damage of the King's Claw")
    @Config.RequiresMcRestart
    public static double kings_claw_damage = 6;

    @Config.Name("Master Parry Sword Cooldown")
    @Config.Comment("Change the cooldown of the Master Parry Sword when landing a successful parry, failing the parry times this amount by 5")
    @Config.RequiresMcRestart
    public static int master_parry_sword_cooldown = 5;
    @Config.Name("Master Parry Sword Damage")
    @Config.Comment("Change the Damage of the Master Parry Sword")
    @Config.RequiresMcRestart
    public static double master_parry_sword_damage = 7;

    @Config.Name("Rah Void Dagger Damage")
    @Config.Comment("Change the Damage of the Rah Void Dagger")
    @Config.RequiresMcRestart
    public static double rah_void_Dagger_damage = 5.5;

    @Config.Name("Rah Void Dagger Cooldown")
    @Config.Comment("Change the Cooldown of the Rah Void Dagger")
    @Config.RequiresMcRestart
    public static int rah_void_dagger_cooldown = 10;

    @Config.Name("Void Catalyst Damage")
    @Config.Comment("Change the damage of the Voidiant Catalyst")
    @Config.RequiresMcRestart
    public static int voidiant_catalyst_damage = 8;

    @Config.Name("Voidiant Catalyst Cooldown")
    @Config.Comment("Change the cooldown of the Voidiant Catalyst in seconds")
    @Config.RequiresMcRestart
    public static int voidiant_catalyst_cooldown = 6;

    @Config.Name("Voidiant Chestplate Charged Capacity")
    @Config.Comment("How many times must the player take damage in order for the Voidiant Chestplate to be fully charged")
    @Config.RequiresMcRestart
    public static int voidiant_chestplate_hit_counter = 9;

    @Config.Name("Voidiant Chestplate Damage")
    @Config.Comment("How much damage does the Voidiant Chestplate do with its blast attack")
    @Config.RequiresMcRestart
    public static float voidiant_chestplate_damage = 6;

    @Config.Name("Voidiant Chestplate Cooldown")
    @Config.Comment("Change the cooldown for the Voidic Chestplate before it can start recharging its ability. In seconds")
    @Config.RequiresMcRestart
    public static int voidiant_chestplate_cooldown = 10;

    @Config.Name("Voidic Helmet Charged Capacity")
    @Config.Comment("How many times must the player take damage in order for the Voidic Helmet to be fully charged")
    @Config.RequiresMcRestart
    public static int voidic_helmet_hit_counter = 12;

    @Config.Name("Voidic Helmet Damage")
    @Config.Comment("Change how much Damage does the Voidic Helmet's Flame Rune AOE do.")
    @Config.RequiresMcRestart
    public static int voidic_helmet_damage = 7;

    @Config.Name("Voidic Helmet Cooldown")
    @Config.Comment("Change the cooldown for the Voidic Helmet before it can start recharging its ability. In seconds")
    @Config.RequiresMcRestart
    public static int voidic_helmet_cooldown = 30;

    @Config.Name("Void Blossom Crown Charged Capacity")
    @Config.Comment("How many times must the player take damage in order for the Void Blossom Crown to use its ability")
    @Config.RequiresMcRestart
    public static int void_blossom_crown_hit_counter = 5;

    @Config.Name("Void Blossom Crown Cooldown")
    @Config.Comment("Change the cooldown for the Void Blossom Crown before it can start recharging its ability. In seconds")
    @Config.RequiresMcRestart
    public static int void_blossom_crown_cooldown = 17;

    @Config.Name("Void Blossom Crown Damage")
    @Config.Comment("When the Void Blossom Crown does its ability, how much damage should the Thorns do?")
    @Config.RequiresMcRestart
    public static int void_blossom_crown_damage = 9;

    @Config.Name("Voidclystic Hammer Projectile Cooldown")
    @Config.Comment("Change the cooldown of the Voidclystic Hammer when launching projectiles. In seconds")
    @Config.RequiresMcRestart
    public static int void_hammer_projectile_cooldown = 14;

    @Config.Name("Voidclystic Hammer Projectile Damage")
    @Config.Comment("Change the damage of the Voidclystic Hammer when launching projectiles.")
    @Config.RequiresMcRestart
    public static float void_hammer_projectile_damage = 9;

    @Config.Name("Voidclystic Hammer Black Hole Cooldown")
    @Config.Comment("Change the cooldown of the Voidclystic Hammer when releasing a Black Hole. In seconds")
    @Config.RequiresMcRestart
    public static int void_hammer_blackhole_cooldown = 60;

    @Config.Name("Voidclystic Hammer Black Hole Damage Cap")
    @Config.Comment("Change the damage cap of the Black hole on its blast. Since the hammer damages based on enchantments with weapons. There is a cap to prevent overpowered combos")
    @Config.RequiresMcRestart
    public static float void_hammer_blackhole_damage_cap = 16;

    @Config.Name("Voidclystic Hammer Damage")
    @Config.Comment("Change the base damage the Voidclystic Hammer")
    @Config.RequiresMcRestart
    public static float void_hammer_damage = 8;

    @Config.Name("Voidclystic Staff Damage")
    @Config.Comment("Change the base damage the Voidclystic Staff")
    @Config.RequiresMcRestart
    public static double void_staff_damage = 4.5;

    @Config.Name("Voidclystic Staff Full Health Modifier")
    @Config.Comment("Change the extra damage bonus for when the player is full health using the Voidclystic Staff")
    @Config.RequiresMcRestart
    public static double void_staff_health_modif = 2;

    @Config.Name("Voidclystic Staff Cooldown")
    @Config.Comment("Change the cooldown of the Voidclystic Staff for the projectile attack. Other ability scales with this. In seconds")
    @Config.RequiresMcRestart
    public static int void_staff_cooldown = 3;

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
    public static int lich_search_distance = 150;

    @Config.Name("Void Blossom Cave Search Radius")
    @Config.Comment("Change how far the Void Lily and Locate command searches for a Void Blossoms Cave. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int void_blosom_search_distance = 120;

    @Config.Name("Burning Flame Arena Search Radius")
    @Config.Comment("Change how far The Spark of Ambition searches for the the Burning Flame Arena. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int burning_flame_arena_search_radius = 130;

    @Config.Name("Frozen Castle Search Radius")
    @Config.Comment("Change how far the Frozen Soul Star and Locate command searches for a Frozen Castle. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int frozen_castle_search_distance = 185;

    @Config.Name("Forgotten Temple Search Radius")
    @Config.Comment("Change how far the Desolate Trinket and Locate command searches for a Forgotten Temple. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int forgotten_temple_distance = 175;

    @Config.Name("Rotten Hold Search Radius")
    @Config.Comment("Change how far the Rotten Core and Locate command searches for a Rotten Hold. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int rotten_hold_distance = 90;

    @Config.Name("High Court City Search Radius")
    @Config.Comment("Change how far the Orb of Light and command searches for the High Court City. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int high_court_city_search_distance = 225;

    @Config.Name("Obsidilith Arena Search Radius")
    @Config.Comment("Change how far the Orb of Light and command searches for the Obsidilith Arena. In chunks not blocks!")
    @Config.RequiresMcRestart
    public static int obsidilith_arena_search_distance = 120;

    @Config.Name("Soul Star Drops Advancement Requirements")
    @Config.Comment("What advancements are required for players to use and unlock Soul Stars dropping from Mobs")
    @Config.RequiresMcRestart
    public static String[] soul_star_progress = {
            "da:kill_kobf"
    };

    @Config.Name("Cult of Roh Assassins Spawning Advancement Requirements")
    @Config.Comment("What advancements are required for Cult of Rah Assassins spawning around the player naturally at night time")
    @Config.RequiresMcRestart
    public static String[] assassins_spawn_progress = {
            "da:kill_great_wyrk"
    };

    @Config.Name("Cult of Roh Sorcerers Spawning Advancement Requirements")
    @Config.Comment("What advancements are required for Cult of Roh Sorcerers spawning around the player naturally at night time")
    @Config.RequiresMcRestart
    public static String[] sorcerers_spawn_progress = {
            "da:kill_night_lich"
    };

    @Config.Name("Cult of Roh Assassin Spawn Rate")
    @Config.Comment("Change the spawn rate of Cult of Roh Assassins, change to 0 to disable")
    @Config.RequiresMcRestart
    public static int assassin_spawn_rate = 5;

    @Config.Name("Cult of Roh Sorcerer Spawn Rate")
    @Config.Comment("Change the spawn rate of Cult of Roh Sorcerer, change to 0 to disable")
    @Config.RequiresMcRestart
    public static int sorcerer_spawn_rate = 2;

    @Config.Name("Boss Reset Enabled/Disabled")
    @Config.Comment("This setting makes it so that if bosses killed all players around them, they will reset back into a key block with a chest above with the respective key. Allowing players to recover there loot more easily and try again. default: true")
    @Config.RequiresMcRestart
    public static boolean boss_reset_enabled = true;

    @Config.Name("Boss Resummon Enabled/Disabled")
    @Config.Comment("MUST HAVE BOSS RESET ENABLED TO WORK. This system allows players to resummon bosses at there structures at the cost of the Soul Key.")
    @Config.RequiresMcRestart
    public static boolean boss_resummon_enabled = true;

    @Config.Name("Boss Resummon Usage Count")
    @Config.Comment("Change how many times a player can resummon a boss before the block is exhausted. Default 2, meaning on the 3rd resummon and slaying the boss the block will no longer appear")
    @Config.RequiresMcRestart
    public static int boss_resummon_max_uses = 2;

    @Config.Name("Boss Resummon Scaling Health")
    @Config.Comment("Change the percentage of base health gets added on after resummoning a boss. This works as times used resummon * x. Default 20% added health. Change to 0 to disable")
    @Config.RequiresMcRestart
    public static double boss_resummon_added_health = 0.2;

    @Config.Name("Boss Resummon Scaling Attack Damage")
    @Config.Comment("Change the percentage of base attack damage gets added on after resummoning a boss. This works as times used * x. Default 5% added attack damage. Change to 0 disable")
    @Config.RequiresMcRestart
    public static double boss_resummon_added_ad = 0.05;

    @Config.Name("Boss Resummon Cooldown")
    @Config.Comment("Change the cooldown of the Boss Summon Block. In minutes")
    @Config.RequiresMcRestart
    public static int boss_resummon_cooldown = 15;

    @Config.Name("Boss Reset Timer")
    @Config.Comment("A timer before boss reset occurs in seconds, basically if any players are not within the bosses follow radius (sight is not needed) and the boss has engaged any player in the past. This timer will countdown till reset")
    @Config.RequiresMcRestart
    public static int boss_reset_timer = 25;

    @Config.Name("Soul Star Advancement Requirement Enable/Disable")
    @Config.Comment("Change to false to disable advancement requirements for Soul Stars to drop")
    @Config.RequiresMcRestart
    public static boolean advancements_block_soul_stars = true;


    @Config.RequiresMcRestart
    @Config.Comment(value = "What blocks can bosses NOT break when doing certain attacks. Chests, Beds, Command Blocks, and Bedrock are already Blacklisted! Works with 'modID:blockName'.")
    public static String[] banned_break_blocks = new String[] {
            "minecraft:crafting_table",
            "minecraft:furnace"
    };

    @Config.Name("Boss Enraged State Enable/Disable")
    @Config.Comment("When enabled, when a boss is below a set health, there attack damage will increase. Set to false to disable entirely")
    @Config.RequiresMcRestart
    public static boolean boss_rage = true;

    @Config.Name("Boss Enraged Health Factor")
    @Config.Comment("When a boss is below this health, it will get a damage boost, 0.1 being 1% health, 1 being 100% health")
    @Config.RequiresMcRestart
    public static double boss_rage_health_factor = 0.15;

    @Config.Name("Boss Enraged Damage Bonus")
    @Config.Comment("When a boss is enraged, it will get a bonus of extra damage based on its base attack damage. 20% of 30 is 6, so its new attack damage will be 36")
    @Config.RequiresMcRestart
    public static double boss_rage_damage_percentage = 0.2;
}
