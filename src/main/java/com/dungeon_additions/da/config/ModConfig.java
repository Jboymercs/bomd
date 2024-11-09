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
    public static float sword_of_ambition_damage = 10;

    @Config.Name("Void Dagger Damage")
    @Config.Comment("Change the damage factor of the Void Dagger")
    @Config.RequiresMcRestart
    public static float void_dagger_damage = 5;

    @Config.Name("Stormvier Rapier Damage")
    @Config.Comment("Change the damage factor of the Stormvier Rapier")
    @Config.RequiresMcRestart
    public static double rapier_damage = 5.5;



}
