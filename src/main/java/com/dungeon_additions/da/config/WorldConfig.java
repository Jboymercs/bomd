package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/world_config")
public class WorldConfig {
    @Config.Name("Void Blossom Cave Weight")
    @Config.Comment("Change the spacing between Void Blossom caves, lower means more frequent, higher means less")
    @Config.RequiresMcRestart
    public static int void_blossom_cave_weight = 300;

    @Config.Name("Void Blossom Arena Dimensions allowed in!")
    @Config.Comment("Take note that any ocean type biomes this structure will NOT spawn in, but you can select which dimension you'd like the structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions = {0};

    @Config.Name("Void Blossom Arena Allowed Biomes to spawn in")
    @Config.Comment("Ocean Biomes will automatically be blacklisted, this is cause of the way the cave spawns it will intefere and flood it. Otherwise, check the whitelist/blacklist if you want this list to be either or.")
    @Config.RequiresMcRestart
    public static String[] biome_allowed = {"minecraft:ice_flats"};

    @Config.Name("Void Blossom Arena Biome Whitelist/Blacklist")
    @Config.Comment("When set too true, the biome list in this config becomes a blacklist of biomes for it to not spawn in, if set to false it becomes a white list. Default: true")
    @Config.RequiresMcRestart
    public static boolean isBlacklist = true;

    @Config.Name("Void Blossom Top Part Enable/Disalbe")
    @Config.Comment("Change if the top part to indicate a Void Blossom Cave is underneath to not spawn, Default: true")
    @Config.RequiresMcRestart
    public static boolean top_part_spawn = true;

    @Config.Name("Void Blossom Cave Height Random")
    @Config.Comment("Change the random variable that is selected for the y height of the blossom cave.")
    @Config.RequiresMcRestart
    public static int y_height_random = 20;

    @Config.Name("Void Blossom Cave Height static")
    @Config.Comment("Change the static variable of the y height, this means it will a minimum spawn x blocks from 0 always")
    @Config.RequiresMcRestart
    public static int y_height_static = 7;

    @Config.Name("Void Blossom Cave Top Part Max height to Spawn")
    @Config.Comment("Change the max block the Void Blossom Top tries to find ground from, mostly used for dimensions with different world height levels compared to vanilla")
    @Config.RequiresMcRestart
    public static int max_top_part = 240;

    @Config.Name("Void Blossom Cave Top Part Min height to Spawn")
    @Config.Comment("Change the min block the Void Blossom Top tries to find ground from, mostly used for dimensions with different world height levels compared to vanilla")
    @Config.RequiresMcRestart
    public static int min_top_part = 40;

    @Config.Name("Burning Flame Arena Spawn Weight")
    @Config.Comment("Change the spawn weight of the Burning Flame Arena (The Nether Dungeon), higher number means lower frequency")
    @Config.RequiresMcRestart
    public static int burning_arena_weight = 120;

    @Config.Name("Burning Flame Arena Size")
    @Config.Comment("Change the size of the Burning Flame Arena, basically how many extra pieces are added onto the boss arena")
    @Config.RequiresMcRestart
    public static int burning_arena_size = 4;

    @Config.Name("Burning Flame Arena Chest Spawns")
    @Config.Comment("Change the spawn weight of chests in the Nether Dungeon")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int burning_arena_chest_spawns = 6;

    @Config.Name("Burning Flame Arena Mob Spawns")
    @Config.Comment("Change the spawn weight of Aberrant Spirits in the Nether Dungeon")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int burning_arena_mob_spawns = 5;

    @Config.Name("Burning Flame Arena Y Level")
    @Config.Comment("Change the y level height the nether dungeon spawns at")
    @Config.RequiresMcRestart
    public static int burning_arena_y_level = 75;

    @Config.Name("Rotten Hold Mob Spawn Chance")
    @Config.Comment("Change the spawn weight of mob spawns in the Rotten Hold, higher is lesser chance")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int rot_hold_mob_spawns = 5;

    @Config.Name("Rotten Hold Chest Chance")
    @Config.Comment("Change the spawn weight of chest spawns in the Rotten Hold, higher is lesser chance")
    @Config.RangeInt(min = 1, max = 10)
    @Config.RequiresMcRestart
    public static int rot_hold_chest_spawns = 5;

    @Config.Name("Rotten Hold Allowed Biomes to spawn in")
    @Config.Comment("Ocean Biomes will automatically be blacklisted, other any biomes added into this list are not locations for the Rotten Hold to spawn in.")
    @Config.RequiresMcRestart
    public static String[] biome_allowed_rot_hold = {
            "minecraft:extreme_hills","minecraft:river","minecraft:frozen_river","minecraft:frozen_ocean","minecraft:beaches","minecraft:desert","minecraft:desert_hills","minecraft:ice_flats","minecraft:ice_mountains","minecraft:stone_beach","minecraft:cold_beach","minecraft:taiga_cold_hills","minecraft:extreme_hills_with_trees","minecraft:mutated_ice_flats","minecraft:mutated_mesa","minecraft:mutated_mesa_rock","minecraft:mutated_mesa_clear_rock","minecraft:mesa_rock","minecraft:mesa_clear_rock","minecraft:mesa"
    };

    @Config.Name("Rotten Hold Whitelist/Blacklist")
    @Config.Comment("Change if the list of biomes is a whitelist for biomes for it too only spawn in, otherwise it is a blacklist of biomes too not spawn in")
    @Config.RequiresMcRestart
    public static boolean rotten_hold_is_blacklist = true;

    @Config.Name("Rotten Hold Spawn Weight")
    @Config.Comment("Spacing of Rotten Hold between the next one, Higher means further apart, lower means closer. This number will not add to spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int rot_hold_spacing = 95;

    @Config.Name("Rotten Hold Minimum Y")
    @Config.Comment("Change the minimum y the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_min_y = 60;

    @Config.Name("Rotten Hold Maximum Y")
    @Config.Comment("Change the maximum y of the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_max_y = 70;
}