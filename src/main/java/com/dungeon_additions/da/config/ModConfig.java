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


}
