package com.dungeon_additions.da.world.nether_arena;

import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenCenterPiece extends WorldGenStructure {

    public WorldGenCenterPiece(String structureName) {
        super("nether_arena/piece/" + structureName);

    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return super.generate(worldIn, rand, position.add(-2, 0, -2));
    }
}
