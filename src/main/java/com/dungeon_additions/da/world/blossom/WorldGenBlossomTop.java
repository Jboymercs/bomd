package com.dungeon_additions.da.world.blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBlossomTop extends WorldGenStructure {

    public WorldGenBlossomTop(String name) {
        super(name);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int y = getSurfaceHeight(worldIn, position, WorldConfig.min_top_part, WorldConfig.max_top_part);
        return super.generate(worldIn, rand, position.add(0, y - 3, 0));
    }


    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock() && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES
            && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES2 && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG2) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
