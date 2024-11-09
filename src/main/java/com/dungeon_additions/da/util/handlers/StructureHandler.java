package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.world.blossom.BlossomTemplate;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
import com.dungeon_additions.da.world.nether_arena.WorldGenNetherArena;
import com.dungeon_additions.da.world.rot_hold.RottenHoldTemplate;
import com.dungeon_additions.da.world.rot_hold.WorldGenRotHold;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class StructureHandler {
    public static void handleStructureRegistries(){
        MapGenStructureIO.registerStructure(WorldGenBlossomCave.Start.class, "BlossomCave");
        MapGenStructureIO.registerStructureComponent(BlossomTemplate.class, "BCP");

        MapGenStructureIO.registerStructure(WorldGenNetherArena.Start.class, "NetherArena");
        MapGenStructureIO.registerStructureComponent(NetherArenaTemplate.class, "NAP");

        MapGenStructureIO.registerStructure(WorldGenRotHold.Start.class, "RottenHold");
        MapGenStructureIO.registerStructureComponent(RottenHoldTemplate.class, "RHP");
    }
}
