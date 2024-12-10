package com.dungeon_additions.da.world.ore_gen;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class BOMDOreGen implements IWorldGenerator {


    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        //PetroGloom
        if(isAllowedDimensionTooSpawnIn(world.provider.getDimension()) && WorldConfig.enabled_petrogloom) {
            genOre(ModBlocks.PETROGLOOM_ROUGH.getDefaultState(), ModRand.range(10, 32), WorldConfig.gloom_chances, WorldConfig.gloom_min_height, WorldConfig.gloom_max_height, world, random, chunkX, chunkZ);
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
}
