package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/world_config")
public class WorldConfig {
    @Config.Name("Void Blossom Cave Weight")
    @Config.Comment("Change the spacing between Void Blossom caves, lower means more frequent, higher means less")
    @Config.RequiresMcRestart
    public static int void_blossom_cave_weight = 60;

    @Config.Name("Void Blossom Arena Dimensions allowed in!")
    @Config.Comment("Take note that any ocean type biomes this structure will NOT spawn in, but you can select which dimension you'd like the structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions = {0};

    @Config.Name("Void Blossom Cave Blacklisted Biome Types")
    @Config.Comment("Block certain biome types from the Void Blossom Cave spawning in, Examples being HOT, COLD, OCEAN")
    @Config.RequiresMcRestart
    public static String[] biome_types_blossom = {"OCEAN","COLD"
    };

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


    @Config.Name("Rotten Hold Dimensions allowed in!")
    @Config.Comment("Take note that any ocean type biomes this structure will NOT spawn in, but you can select which dimension you'd like the structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_rotten_hold = {0};
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
    public static int rot_hold_spacing = 180;

    @Config.Name("Rotten Hold Minimum Y")
    @Config.Comment("Change the minimum y the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_min_y = 60;

    @Config.Name("Rotten Hold Maximum Y")
    @Config.Comment("Change the maximum y of the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_max_y = 70;

    @Config.Name("Night Lich Tower Dimensions allowed in!")
    @Config.Comment("Take note that any ocean type biomes this structure will NOT spawn in, but you can select which dimension you'd like the structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_lich_tower = {0};

    @Config.Name("Night Lich Tower Minimum Y")
    @Config.Comment("Change the minimum y the surface part of this structure can spawn at")
    @Config.RequiresMcRestart
    public static int lich_tower_min_y = 50;

    @Config.Name("Night Lich Tower Maximum Y")
    @Config.Comment("Change the maximum y the surface part of this structure can spawn at")
    @Config.RequiresMcRestart
    public static int lich_tower_max_y = 75;

    @Config.Name("Night Lich Tower Spacing")
    @Config.Comment("Change the spacing between each Night Lich tower, higher means further apart, lower means closer. This number will not add to spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int lich_tower_spacing = 70;

    @Config.Name("Night Lich Blacklisted Biome Types")
    @Config.Comment("Add Biome types that DISALLOW the Night Lich Tower from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] biome_types_blacklist_lich = {"DENSE","NETHER","END","MUSHROOM","OCEAN","RIVER","MESA","MOUNTAIN","HILLS","BEACH"
    };

    @Config.Name("Petrogloom Ore Gen")
    @Config.Comment("Allowed Dimension for Petrogloom to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_petrogloom = {0};

    @Config.Name("Petrogloom Ore Gen Enabled/Disabled")
    @Config.Comment("Change if Petrogloom can generate throughout the world. default : true")
    @Config.RequiresMcRestart
    public static boolean enabled_petrogloom = true;

    @Config.Name("Petrogloom Ore Gen Spawn Chances")
    @Config.Comment("Spawn chances of Petrogloom")
    @Config.RequiresMcRestart
    public static int gloom_chances = 1;

    @Config.Name("Petrogloom Ore Gen Min Height")
    @Config.Comment("Minimum Spawn Height of Petrogloom")
    @Config.RequiresMcRestart
    public static int gloom_min_height = 15;

    @Config.Name("Petrogloom Ore Gen Max Height")
    @Config.Comment("Maximum Spawn Height of Petrogloom")
    @Config.RequiresMcRestart
    public static int gloom_max_height = 30;

}
