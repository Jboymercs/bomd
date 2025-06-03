package com.dungeon_additions.da.world.forgotten_temple.parts;

import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTempleTop extends WorldGenStructure {


    public WorldGenTempleTop(String structureName) {
        super("forgotten_temple/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return super.generate(worldIn, rand, position.add(-2, 1, -2));
    }
}
