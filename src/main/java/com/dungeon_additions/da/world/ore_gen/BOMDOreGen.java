package com.dungeon_additions.da.world.ore_gen;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModRand;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.List;
import java.util.Random;

public class BOMDOreGen implements IWorldGenerator {


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        //PetroGloom
        if(isAllowedDimensionTooSpawnIn(world.provider.getDimension()) && WorldConfig.enabled_petrogloom) {
            genOre(ModBlocks.PETROGLOOM_ROUGH.getDefaultState(), ModRand.range(18, 32), WorldConfig.gloom_chances, WorldConfig.gloom_min_height, WorldConfig.gloom_max_height, world, random, chunkX, chunkZ);
        }
        //Farum
        if(isAllowedDimensionTooSpawnInFarum(world.provider.getDimension()) && WorldConfig.enabled_farum && isAbleToSpawnHereFarum(new BlockPos(chunkX << 4, 0, chunkZ << 4), world)) {
            genOre(ModBlocks.FARUM_STONE.getDefaultState(), ModRand.range(24, 34), WorldConfig.farum_chances, WorldConfig.farum_min_height, WorldConfig.farum_max_height, world, random, chunkX, chunkZ);
        }
    }


    private void genOre(IBlockState block, int size, int spawnChances, int minHeight, int maxHeight, World world, Random rand, int chunkX, int chunkZ){
        WorldGenBOMDStone generator = new WorldGenBOMDStone(size, block);
        int dy =  maxHeight - minHeight +1;
        if (dy < 1) return;
        for (int i = 0; i < spawnChances; i++){
            int x = chunkX * 16 + rand.nextInt(16);
            int y = minHeight + rand.nextInt(dy);
            int z = chunkZ * 16 + rand.nextInt(16);
            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }


    public static boolean isAllowedDimensionTooSpawnIn(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_petrogloom) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }

    public static boolean isAllowedDimensionTooSpawnInFarum(int dimensionIn) {
        for(int i : WorldConfig.list_of_dimensions_farum) {
            if(i == dimensionIn)
                return true;
        }

        return false;
    }


    public static boolean isAbleToSpawnHereFarum(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesFarum()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> farumSpawnBiomes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesFarum() {
        if(farumSpawnBiomes == null) {
            farumSpawnBiomes = Lists.newArrayList();

            for(String str : WorldConfig.farum_allowed_biomes) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) farumSpawnBiomes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return farumSpawnBiomes;
    }
}
