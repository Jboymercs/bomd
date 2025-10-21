package com.dungeon_additions.da.util.handlers;

import com.dungeon_additions.da.world.blossom.BlossomTemplate;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import com.dungeon_additions.da.world.forgotten_temple.ForgottenTempleTemplate;
import com.dungeon_additions.da.world.forgotten_temple.WorldGenForgottenTemple;
import com.dungeon_additions.da.world.frozen_castle.FrozenCastleTemplate;
import com.dungeon_additions.da.world.frozen_castle.WorldGenFrozenCastle;
import com.dungeon_additions.da.world.gaelon_sanctuary.GaelonSanctuaryTemplate;
import com.dungeon_additions.da.world.gaelon_sanctuary.WorldGenGaelonSanctuary;
import com.dungeon_additions.da.world.high_city.HighCityTemplate;
import com.dungeon_additions.da.world.high_city.WorldGenHighCity;
import com.dungeon_additions.da.world.lich_tower.LichTowerTemplate;
import com.dungeon_additions.da.world.lich_tower.WorldGenLichTower;
import com.dungeon_additions.da.world.nether_arena.NetherArenaTemplate;
import com.dungeon_additions.da.world.nether_arena.WorldGenNetherArena;
import com.dungeon_additions.da.world.obsidilith_arena.ObsidilithArenaTemplate;
import com.dungeon_additions.da.world.obsidilith_arena.WorldGenObsidilithArena;
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
        //Frozen Castle
        MapGenStructureIO.registerStructure(WorldGenFrozenCastle.Start.class, "FrozenCastle");
        MapGenStructureIO.registerStructureComponent(FrozenCastleTemplate.class, "FCP");
        //High City
        MapGenStructureIO.registerStructure(WorldGenHighCity.Start.class, "HighCourtCity");
        MapGenStructureIO.registerStructureComponent(HighCityTemplate.class, "HCP");
        //Forgotten Temple
        MapGenStructureIO.registerStructure(WorldGenForgottenTemple.Start.class, "ForgottenTemple");
        MapGenStructureIO.registerStructureComponent(ForgottenTempleTemplate.class, "FTD");
        //Obsidilith Arena
        MapGenStructureIO.registerStructure(WorldGenObsidilithArena.Start.class, "ObsidilithArena");
        MapGenStructureIO.registerStructureComponent(ObsidilithArenaTemplate.class, "OAP");
        //Gaelon Sanctuary
        MapGenStructureIO.registerStructure(WorldGenGaelonSanctuary.Start.class, "GaelonSanctuary");
        MapGenStructureIO.registerStructureComponent(GaelonSanctuaryTemplate.class, "GSP");
    }
}
