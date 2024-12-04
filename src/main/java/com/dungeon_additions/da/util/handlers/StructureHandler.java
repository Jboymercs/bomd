package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.world.blossom.BlossomTemplate;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import com.dungeon_additions.da.world.lich_tower.LichTowerTemplate;
import com.dungeon_additions.da.world.lich_tower.WorldGenLichTower;
import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
import com.dungeon_additions.da.world.nether_arena.WorldGenNetherArena;
import com.dungeon_additions.da.world.rot_hold.RottenHoldTemplate;
import com.dungeon_additions.da.world.rot_hold.WorldGenRotHold;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class StructureHandler {
    public static void handleStructureRegistries(){
        //Void Blossom
        MapGenStructureIO.registerStructure(WorldGenBlossomCave.Start.class, "BlossomCave");
        MapGenStructureIO.registerStructureComponent(BlossomTemplate.class, "BCP");
        //Burning Flame Arena
        MapGenStructureIO.registerStructure(WorldGenNetherArena.Start.class, "NetherArena");
        MapGenStructureIO.registerStructureComponent(NetherArenaTemplate.class, "NAP");
        //Rotten Hold
        MapGenStructureIO.registerStructure(WorldGenRotHold.Start.class, "RottenHold");
        MapGenStructureIO.registerStructureComponent(RottenHoldTemplate.class, "RHP");
        //Night Lich
        MapGenStructureIO.registerStructure(WorldGenLichTower.Start.class, "NightLichTower");
        MapGenStructureIO.registerStructureComponent(LichTowerTemplate.class, "LTP");
    }
}
