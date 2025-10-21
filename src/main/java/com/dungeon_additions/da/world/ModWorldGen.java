package com.dungeon_additions.da.world;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import com.dungeon_additions.da.world.forgotten_temple.WorldGenForgottenTemple;
import com.dungeon_additions.da.world.frozen_castle.WorldGenFrozenCastle;
import com.dungeon_additions.da.world.gaelon_sanctuary.WorldGenGaelonSanctuary;
import com.dungeon_additions.da.world.high_city.WorldGenHighCity;
import com.dungeon_additions.da.world.lich_tower.WorldGenLichTower;
import com.dungeon_additions.da.world.nether_arena.WorldGenNetherArena;
import com.dungeon_additions.da.world.obsidilith_arena.WorldGenObsidilithArena;
import com.dungeon_additions.da.world.rot_hold.WorldGenRotHold;
import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    public static final WorldGenBlossomCave blossomCave = new WorldGenBlossomCave();
    public static final WorldGenRotHold rotten_hold = new WorldGenRotHold();

    public static final WorldGenLichTower lich_tower = new WorldGenLichTower();
    private static final WorldGenNetherArena netherArena = new WorldGenNetherArena();

    private static final WorldGenFrozenCastle frozen_castle = new WorldGenFrozenCastle();

    private static final WorldGenHighCity high_court_city = new WorldGenHighCity();
    private static final WorldGenForgottenTemple forgotten_temple = new WorldGenForgottenTemple();

    private static final WorldGenObsidilithArena obsidilithArena = new WorldGenObsidilithArena();
    private static final WorldGenGaelonSanctuary gaelon_sanctuary = new WorldGenGaelonSanctuary();
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);

        //nether arena
        if(world.provider.getDimension() == -1 && WorldConfig.burning_flame_arena_enabled) {
                netherArena.generate(world, random, pos);
        }
        //void blossom
            if (isAllowedDimensionTooSpawnIn(world.provider.getDimension()) && WorldConfig.void_cave_enabled) {
                            BlockPos posModified = new BlockPos(pos.getX(), 0, pos.getZ());
                            blossomCave.generate(world, random, posModified);
            }
        //Rotten Hold
        if (isAllowedDimensionTooSpawnInRottenHold(world.provider.getDimension()) && WorldConfig.rotten_hold_enabled) {
            if(world.provider.getBiomeForCoords(pos) != Biomes.DEEP_OCEAN && world.provider.getBiomeForCoords(pos) != Biomes.OCEAN) {
                    rotten_hold.generate(world, random, pos);
            }
        }
        //Night Lich Tower
        if(isAllowedDimensionTooSpawnInNightLich(world.provider.getDimension()) && WorldConfig.night_lich_tower_enabled) {
            //After doing solid ground checks it can signal for the lich tower to try and generate
            lich_tower.generate(world, random, pos);
        }
        //Frozen City
        if(isAllowedDimensionTooSpawnInFrozenCastle(world.provider.getDimension()) && WorldConfig.frozen_castle_enabled) {
            frozen_castle.generate(world, random, pos);
        }
        //High Court City
        if(isAllowedDimensionTooSpawnInHighCourtCity(world.provider.getDimension()) && WorldConfig.hcc_enabled) {
            high_court_city.generate(world, random, pos);
        }
        //Forgotten Temple
        if(isAllowedDimensionTooSpawnInForgottenTemple(world.provider.getDimension()) && WorldConfig.temple_enabled) {
            forgotten_temple.generate(world, random, pos);
        }
        //Obsidilith Arena
        if(isAllowedDimensionTooSpawnInObsidilithArena(world.provider.getDimension()) && WorldConfig.obsidilith_arena_enabled) {
            obsidilithArena.generate(world, random, pos);
        }
        //Gaelon Sanctuary
        if(isAllowedDimensionTooSpawnInGaelonSanctuary(world.provider.getDimension()) && WorldConfig.gaelon_sanctuary_enabled) {
            gaelon_sanctuary.generate(world, random, pos);
        }


        }


    /**
     * Credit goes to SmileyCorps for Biomes read from a config
     * @return
     */


    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInFrozenCastle(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_frozen_castle) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInHighCourtCity(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_high_court_city) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInRottenHold(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_rotten_hold) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInNightLich(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_lich_tower) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInForgottenTemple(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_forgotten_temple) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInObsidilithArena(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_obsidilith_arena) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInGaelonSanctuary(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_gaelon_sanctuary) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }
}
