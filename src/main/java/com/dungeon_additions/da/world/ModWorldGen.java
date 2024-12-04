package com.dungeon_additions.da.world;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import com.dungeon_additions.da.world.lich_tower.WorldGenLichTower;
import com.dungeon_additions.da.world.nether_arena.WorldGenNetherArena;
import com.dungeon_additions.da.world.rot_hold.WorldGenRotHold;
import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ModWorldGen implements IWorldGenerator {

    public static final WorldGenBlossomCave blossomCave = new WorldGenBlossomCave();
    public static final WorldGenRotHold rotten_hold = new WorldGenRotHold();

    public static final WorldGenLichTower lich_tower = new WorldGenLichTower();
    private static final WorldGenNetherArena netherArena = new WorldGenNetherArena();
    private static List<Biome> spawnBiomes;
    private static List<Biome> spawnBiomesRottenHold;

    private static List<Biome> spawnBiomesLichTower;
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);

        //nether arena
        if(world.provider.getDimension() == -1) {
                netherArena.generate(world, random, pos);
        }

        //void blossom
            if (isAllowedDimensionTooSpawnIn(world.provider.getDimension())) {

                if (world.provider.getBiomeForCoords(pos) != Biomes.OCEAN && world.getBiomeForCoordsBody(pos) != Biomes.DEEP_OCEAN) {
                    if (WorldConfig.isBlacklist == (world.provider.getBiomeForCoords(pos) != getSpawnBiomes().iterator())) {
                            BlockPos posModified = new BlockPos(pos.getX(), 0, pos.getZ());
                            blossomCave.generate(world, random, posModified);
                    }
                }
            }
        //Rotten Hold
        if (isAllowedDimensionTooSpawnInRottenHold(world.provider.getDimension())) {
            if(world.provider.getBiomeForCoords(pos) != Biomes.DEEP_OCEAN && world.provider.getBiomeForCoords(pos) != Biomes.OCEAN) {
                if(WorldConfig.rotten_hold_is_blacklist == (world.provider.getBiomeForCoords(pos) != getSpawnBiomesRottenHold().iterator())) {
                    BlockPos posModified = new BlockPos(pos.getX(), 0, pos.getZ());
                    rotten_hold.generate(world, random, posModified);
                }
            }
        }
        //Night Lich Tower
        if(isAllowedDimensionTooSpawnInNightLich(world.provider.getDimension())) {
            if(world.provider.getBiomeForCoords(pos) != Biomes.DEEP_OCEAN && world.provider.getBiomeForCoords(pos) != Biomes.OCEAN) {
                if(WorldConfig.night_lich_is_blacklist == (world.provider.getBiomeForCoords(pos) != getSpawnBiomesLichTower().iterator())) {
                    BlockPos posModified = new BlockPos(pos.getX(), 0, pos.getZ());
                    lich_tower.generate(world, random, posModified);
                }
            }
        }

        }


    /**
     * Credit goes to SmileyCorps for Biomes read from a config
     * @return
     */
    public static List<Biome> getSpawnBiomes() {
        if (spawnBiomes == null) {
            spawnBiomes = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomes.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomes;
    }

    public static List<Biome> getSpawnBiomesRottenHold() {
        if (spawnBiomesRottenHold == null) {
            spawnBiomesRottenHold = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed_rot_hold) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomesRottenHold.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomesRottenHold;
    }

    public static List<Biome> getSpawnBiomesLichTower() {
        if (spawnBiomesLichTower == null) {
            spawnBiomesLichTower = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed_lich) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomesLichTower.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomesLichTower;
    }

    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions) {
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
}
