package com.dungeon_additions.da.config;

import com.dungeon_additions.da.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = "BOMD DA Backport/world_config")
public class WorldConfig {
    @Config.Name("Void Blossom Cave Weight")
    @Config.Comment("Change the spacing between Void Blossom caves, lower means more frequent, higher means less")
    @Config.RequiresMcRestart
    public static int void_blossom_cave_weight = 100;

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

    @Config.Name("Void Blossom Cave Enabled/Disabled")
    @Config.Comment("When set to false, the Void Blossom Cave will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean void_cave_enabled = true;

    @Config.Name("Burning Flame Arena Spawn Weight")
    @Config.Comment("Change the spawn weight of the Burning Flame Arena (The Nether Dungeon), higher number means lower frequency")
    @Config.RequiresMcRestart
    public static int burning_arena_weight = 80;

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

    @Config.Name("Burning Flame Arena Blacklisted Biome Types")
    @Config.Comment("Add Biome types that DISALLOW the Burning Flame Arena from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] biome_types_blacklist_burning_arena = {"COLD","MOUNTAIN"
    };

    @Config.Name("Burning Flame Arena Enabled/Disabled")
    @Config.Comment("When set to false, the Burning Flame Arena will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean burning_flame_arena_enabled = true;

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

    @Config.Name("Rotten Hold WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW the Rotten Hold from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] rotten_hold_whitelist = {"PLAINS","FOREST","SWAMP","DENSE"
    };

    @Config.Name("Rotten Hold Spawn Weight")
    @Config.Comment("Change the spacing between each Rotten Hold, higher means further apart, lower means closer. This number will not add to spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int rot_hold_spacing = 55;

    @Config.Name("Rotten Hold Minimum Y")
    @Config.Comment("Change the minimum y the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_min_y = 60;

    @Config.Name("Rotten Hold Maximum Y")
    @Config.Comment("Change the maximum y of the surface part of this structure can spawn at, the dungeon will always follow along with what the surface part is")
    @Config.RequiresMcRestart
    public static int rot_hold_max_y = 70;

    @Config.Name("Rotten Hold Start Y Level")
    @Config.Comment("Change the y level that the dungeon part of the Rotten Hold generates at.")
    @Config.RequiresMcRestart
    public static int rot_hold_static_y = 40;

    @Config.Name("Rotten Hold Enabled/Disabled")
    @Config.Comment("When set to false, the Rotten Hold will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean rotten_hold_enabled = true;

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
    public static int lich_tower_spacing = 150;

    @Config.Name("Night Lich Blacklisted Biome Types")
    @Config.Comment("Add Biome types that DISALLOW the Night Lich Tower from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] biome_types_blacklist_lich = {"DENSE","NETHER","END","MUSHROOM","OCEAN","RIVER","MESA","MOUNTAIN","HILLS","BEACH"
    };

    @Config.Name("Night Lich Tower Enabled/Disabled")
    @Config.Comment("When set to false, the Night Lich Tower will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean night_lich_tower_enabled = true;

    @Config.Name("Frozen Castle WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW the Frozen Castle from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] frozen_castle_blacklist = {"COLD","SNOWY","WASTELAND","MOUNTAIN"
    };

    @Config.Name("Frozen Castle Spacing")
    @Config.Comment("Change the spacing between each Frozen Castle, higher means further apart, lower means closer. This number will not add to the spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int frozen_castle_spacing = 35;

    @Config.Name("Frozen Castle Y Height")
    @Config.Comment("Change the value that the Frozen Castle generates at Y level")
    @Config.RequiresMcRestart
    public static int frozen_castle_y_height = 26;

    @Config.Name("Frozen Castle Size")
    @Config.Comment("Change the size of the Frozen Castle")
    @Config.RequiresMcRestart
    public static int frozen_castle_size = 5;

    @Config.Name("Frozen Castle Mob Spawn Weight")
    @Config.Comment("Change the chance of mob spawns throughout the Frozen Castle, higher number means more spawns.")
    @Config.RangeInt(min = 1, max = 10)
    public static int frozen_castle_mob_spawns = 5;

    @Config.Name("Frozen Castle Chest Chance Weight")
    @Config.Comment("Change the chance of chest spawns throughout the Frozen Castle, higher number means more chests")
    @Config.RangeInt(min = 1, max = 10)
    public static int frozen_castle_chest_spawns = 5;

    @Config.Name("Frozen Castle Secondary Champion Spawn Chances")
    @Config.Comment("Change the chance of the Draugr Champion spawning more than once, there is always a guarantee for one to spawn per dungeon. Higher number means more spawns")
    @Config.RangeInt(min = 1, max = 10)
    public static int frozen_castle_big_mob_chance = 2;

    @Config.Name("Frozen Castle Allowed Dimensions")
    @Config.Comment("Select what dimensions this structure is allowed to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_frozen_castle = {0};

    @Config.Name("Frozen Castle Enabled/Disabled")
    @Config.Comment("When set to false, the Frozen Castle will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean frozen_castle_enabled = true;

    @Config.Name("High Court City Blacklisted Biome Types")
    @Config.Comment("Add Biome types that DISALLOW the High City from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] biome_types_blacklist_high_city = {"MOUNTAIN"
    };

    @Config.Name("High Court City Mob Spawn Weight")
    @Config.Comment("Change the chance of mob spawns throughout the High Court City, higher number means more spawns.")
    @Config.RangeInt(min = 1, max = 10)
    public static int high_city_mob_spawns = 5;

    @Config.Name("High Court City Chest Chance Weight")
    @Config.Comment("Change the chance of chest spawns throughout the High Court City, higher number means more chests")
    @Config.RangeInt(min = 1, max = 10)
    public static int high_city_chest_spawns = 6;

    @Config.Name("High Court City Spacing")
    @Config.Comment("Change the spacing between each High Court City, higher means further apart, lower means closer. This number will not add to the spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int high_city_spacing = 220;

    @Config.Name("High Court City Size")
    @Config.Comment("Change the size of the High Court City")
    @Config.RequiresMcRestart
    public static int high_court_city_size = 4;

    @Config.Name("High Court City Y Height")
    @Config.Comment("Change the y Height the High Court City generates at")
    @Config.RequiresMcRestart
    public static int high_city_y_height = 185;

    @Config.Name("High Court City Allowed Dimensions")
    @Config.Comment("Select what dimensions this structure is allowed to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_high_court_city = {0};

    @Config.Name("High Court City Enabled/Disabled")
    @Config.Comment("When set to false, the High Court City will be disabled from world generation.")
    @Config.RequiresMcRestart
    public static boolean hcc_enabled = true;

    @Config.Name("Forgotten Temple Y Height")
    @Config.Comment("Change the Y heigt that the puzzle room generates in the Forgotten Temple")
    @Config.RequiresMcRestart
    public static int temple_y_height = 40;

    @Config.Name("Forgotten Temple Max Y Height")
    @Config.Comment("This paramater ensures that the surface part doesn't go above a set y value. In case it spawns in mountains or something of that")
    @Config.RequiresMcRestart
    public static int temple_max_y_height = 70;

    @Config.Name("Forgotten Temple Spacing")
    @Config.Comment("Change how rare Forgotten Temple structures are, increasing the value increases the spacing")
    @Config.RequiresMcRestart
    public static int temple_spacing = 65;

    @Config.Name("Forgotten Temple WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW the Forgotten Temple from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] forgotten_temple_whitelist = {"HOT","DRY","SANDY",
    };

    @Config.Name("Forgotten Temple Size")
    @Config.Comment("Change the size of the Forgotten Temple")
    @Config.RequiresMcRestart
    public static int forgotten_temple_size = 5;

    @Config.Name("Forgotten Temple Dimensions allowed in!")
    @Config.Comment("Take note that any ocean type biomes this structure will NOT spawn in, but you can select which dimension you'd like the structure to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_forgotten_temple = {0};

    @Config.Name("Forgotten Temple Enabled/Disabled")
    @Config.Comment("Change if the Forgotten Temple should spawn at all. default: true")
    @Config.RequiresMcRestart
    public static boolean temple_enabled = true;

    @Config.Name("Obsidilith Arena Spacing")
    @Config.Comment("Change the spacing between each Obsidilith Arena, higher means further apart, lower means closer. This number will not add to the spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int obsidilith_arena_spacing = 110;

    @Config.Name("Obsidilith Arena Y Height")
    @Config.Comment("Change the Y Height at which the Obsidilith Arena generates at.")
    @Config.RequiresMcRestart
    public static int obsidilith_y_height = 105;

    @Config.Name("Obsidilith Arena Mob Spawn Weight")
    @Config.Comment("Change the chance of mob spawns throughout the Obsidilith Arena, higher number means more spawns.")
    @Config.RangeInt(min = 1, max = 10)
    public static int obsidilith_arena_mob_spawns = 4;

    @Config.Name("Obsidilith Arena Chest Chance Weight")
    @Config.Comment("Change the chance of chest spawns throughout the Obsidilith Arena, higher number means more chests")
    @Config.RangeInt(min = 1, max = 10)
    public static int obsidilith_arena_chest_spawns = 5;

    @Config.Name("Obsidilith Arena WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW the Obsidilith Arena from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] obsdilith_arena_whitelist = {"END"
    };

    @Config.Name("Obsidilith Arena Enabled/Disabled")
    @Config.Comment("Change if the Obsidilith Arena should spawn at all. default: true")
    @Config.RequiresMcRestart
    public static boolean obsidilith_arena_enabled = true;

    @Config.Name("Obsidilith Arena Allowed Dimensions")
    @Config.Comment("Select what dimensions this structure is allowed to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_obsidilith_arena = {1};


    @Config.Name("Gaelon Sanctuary WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW the Gaelon Sanctuary from spawning in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] gaelon_sanctuary_whitelist = {"MOUNTAIN","HILLS","SPOOKY"
    };

    @Config.Name("Gaelon Sanctuary Spacing")
    @Config.Comment("Change the spacing between each Gaelon Sanctuary, higher means further apart, lower means closer. This number will not add to the spacing if not in the correct biome")
    @Config.RequiresMcRestart
    public static int gaelon_sanctuary_spacing = 175;

    @Config.Name("Gaelon Sanctuary Max Y Height")
    @Config.Comment("Change the max Y Height at which the Gaelon Sanctuary surface part generates at.")
    @Config.RequiresMcRestart
    public static int gaelon_sanctuary_max_y_height = 120;

    @Config.Name("Gaelon Sanctuary Base Y Height")
    @Config.Comment("Change the base Y Height at which the Gaelon Sanctuary generates at.")
    @Config.RequiresMcRestart
    public static int gaelon_sanctuary_y_height = 10;

    @Config.Name("Gaelon Sanctuary Allowed Dimensions")
    @Config.Comment("Select what dimensions this structure is allowed to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_gaelon_sanctuary = {0};

    @Config.Name("Gaelon Sanctuary Enabled/Disabled")
    @Config.Comment("Change if the Gaelon Sanctuary should spawn at all. default: true")
    @Config.RequiresMcRestart
    public static boolean gaelon_sanctuary_enabled = true;

    @Config.Name("Gaelon Sanctuary Mob Spawn Weight")
    @Config.Comment("Change the chance of mob spawns throughout the Gaelon Sanctuary, higher number means more spawns.")
    @Config.RangeInt(min = 1, max = 10)
    public static int gaelon_sanctuary_mob_spawns = 4;

    @Config.Name("Gaelon Sanctuary Chest Chance Weight")
    @Config.Comment("Change the chance of chest spawns throughout the Gaelon Sanctuary, higher number means more chests")
    @Config.RangeInt(min = 1, max = 10)
    public static int gaelon_sanctuary_chest_spawns = 5;

    @Config.Name("Gaelon Sanctuary Size")
    @Config.Comment("Change the size of the Gaelon Sanctuary dungeon")
    @Config.RequiresMcRestart
    public static int gaelon_sanctuary_size = 4;

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

    @Config.Name("Farum Ore Gen")
    @Config.Comment("Allowed Dimension for Farum to spawn in")
    @Config.RequiresMcRestart
    public static int[] list_of_dimensions_farum = {0};

    @Config.Name("Farum Ore Gen Enabled/Disabled")
    @Config.Comment("Change if Petrogloom can generate throughout the world. default : true")
    @Config.RequiresMcRestart
    public static boolean enabled_farum = true;

    @Config.Name("Farum Ore Gen Spawn Chances")
    @Config.Comment("Spawn chances of Farum")
    @Config.RequiresMcRestart
    public static int farum_chances = 2;

    @Config.Name("Farum Ore Gen Min Height")
    @Config.Comment("Minimum Spawn Height of Farum")
    @Config.RequiresMcRestart
    public static int farum_min_height = 35;

    @Config.Name("Farum Ore Gen Max Height")
    @Config.Comment("Maximum Spawn Height of Farum")
    @Config.RequiresMcRestart
    public static int farum_max_height = 65;

    @Config.Name("Farum WhiteList Biome Types")
    @Config.Comment("Add Biome types that ALLOW Farum to spawn in that biome, examples of tags are HOT, COLD, SPOOKY")
    @Config.RequiresMcRestart
    public static String[] farum_allowed_biomes = {"MOUNTAIN"
    };

}
