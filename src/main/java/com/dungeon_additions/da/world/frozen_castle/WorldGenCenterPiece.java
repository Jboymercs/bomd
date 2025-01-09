package com.dungeon_additions.da.world.frozen_castle;

import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenCenterPiece extends WorldGenStructure {
    public WorldGenCenterPiece(String structureName) {
        super("frozen_castle/" + structureName);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        //loot
        if(function.startsWith("chest")) {

        }

        //mobs
        if(function.startsWith("mob")) {

        } else if (function.startsWith("big_mob")) {

        }
    }
}
