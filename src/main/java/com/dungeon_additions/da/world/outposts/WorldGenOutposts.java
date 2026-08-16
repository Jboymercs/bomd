package com.dungeon_additions.da.world.outposts;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.obsidilith_arena.ObsidilithArena;
import com.dungeon_additions.da.world.obsidilith_arena.WorldGenObsidilithArena;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;
import org.lwjgl.Sys;
import software.bernie.shadowed.fasterxml.jackson.databind.ObjectReader;

import java.util.List;
import java.util.Random;

public class WorldGenOutposts extends WorldGenerator {

    private int spacing;
    private int spacing_nether;
    private int spacing_end;
    private int spacing_overworld;
    private int separation;

    public WorldGenOutposts() {
        this.separation = 16;
        this.spacing_end = WorldConfig.end_outposts_spacing;
        this.spacing_overworld = 100;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        //End
        if(world.provider.getDimension() == 1) {
            if(canSpawnStructureAtPosEnd(world, position.getX() >> 4, position.getZ() >> 4) && WorldConfig.end_outposts_enabled) {
                getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - 250, position.getZ() - 250, position.getX() + 250, position.getZ() + 250));
                return true;
            }
            //Overworld
        } else if (world.provider.getDimension() == 0) {
            if(canSpawnStructureAtPosOverworld(world, position.getX() >> 4, position.getZ() >> 4) && WorldConfig.overworld_outposts_enabled) {
                getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - 250, position.getZ() - 250, position.getX() + 250, position.getZ() + 250));
                return true;
            }
            //Nether
        } else if (world.provider.getDimension() == -1) {

        }
        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenOutposts.Start(world, rand, chunkX, chunkZ);
    }

    protected boolean canSpawnStructureAtPosEnd(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing_end - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing_end - 1;
        }

        int k = chunkX / this.spacing_end;
        int l = chunkZ / this.spacing_end;
        Random random =  world.setRandomSeed(k, l, 60304064);
        k = k * this.spacing_end;
        l = l * this.spacing_end;
        k = k + (random.nextInt(this.spacing_end - this.separation) + random.nextInt(this.spacing_end - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing_end - this.separation) + random.nextInt(this.spacing_end - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHereEnd(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHereEnd(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesEnd()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> endBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesEnd() {
        if(endBiomeTypes == null) {
            endBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.end_outposts_whitelist) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) endBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return endBiomeTypes;
    }

    //OVERWORLD

    protected boolean canSpawnStructureAtPosOverworld(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing_overworld - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing_overworld - 1;
        }

        int k = chunkX / this.spacing_overworld;
        int l = chunkZ / this.spacing_overworld;
        Random random =  world.setRandomSeed(k, l, 60304064);
        k = k * this.spacing_overworld;
        l = l * this.spacing_overworld;
        k = k + (random.nextInt(this.spacing_overworld - this.separation) + random.nextInt(this.spacing_overworld - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing_overworld - this.separation) + random.nextInt(this.spacing_overworld - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHereOverworld(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHereOverworld(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesOverworld()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> overworldBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesOverworld() {
        if(overworldBiomeTypes == null) {
            overworldBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.overworld_outposts_whitelist) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) overworldBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return overworldBiomeTypes;
    }


    public static class Start extends StructureStart {

        private boolean valid;

        public Start() {

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, rand, chunkX, chunkZ);
        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
            //sorts out and spawns outposts variously
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand = random.nextInt(Rotation.values().length);

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);

            //End Outpost
            if(worldIn.provider.getDimension() == 1) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();

                    int y = getSurfaceHeight(worldIn, posI.add(-5, 0, -5), WorldConfig.end_outposts_min_y, WorldConfig.end_outposts_min_y + 20);
                    int y2 = getSurfaceHeight(worldIn, posI.add(5, 0, 5), WorldConfig.end_outposts_min_y, WorldConfig.end_outposts_min_y + 20);
                    int y3 = getSurfaceHeight(worldIn, posI, WorldConfig.end_outposts_min_y, WorldConfig.end_outposts_min_y + 20);
                    OutpostGeneric outpost = new OutpostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    if(y != 0 && y2 != 0) {
                        //generates ground one
                        outpost.startBuilding(new BlockPos(posI.getX() - 14, y3 - 1, posI.getZ() - 14), rotation, "end/end_outpost_ground");
                    } else {
                        //generates sky one
                        if(worldIn.rand.nextInt(2) == 0) {
                            outpost.startBuilding(new BlockPos(posI.getX() - 16, ModRand.range(WorldConfig.end_outposts_min_y + 20, WorldConfig.end_outposts_max_y), posI.getZ() -16), rotation, "end/end_outpost_large");
                        } else {
                            outpost.startBuilding(new BlockPos(posI.getX(), ModRand.range(WorldConfig.end_outposts_min_y + 20, WorldConfig.end_outposts_max_y), posI.getZ()), rotation, "end/end_outpost_sky");
                        }
                    }
                    this.updateBoundingBox();

                    if (this.isSizeableStructure()) {

                        break;
                    }

                }
                //Overworld
            } else if (worldIn.provider.getDimension() == 0) {
                int y = getSurfaceHeight(worldIn, posI, WorldConfig.overworld_outposts_min_y, WorldConfig.overworld_outposts_max_y);
                for (int i = 0; i < 4; i++) {
                    //Rotten Hold
                    if (validBiomeType(BiomeDictionary.Type.FOREST, posI, worldIn) && y != 0) {
                        OutpostGeneric rotten_outpost = new OutpostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        String outpostType = "rotten/outpost_1";
                        int randI = ModRand.range(1, 4);
                        int offsetY = 3;
                        if(randI == 2) {
                            outpostType = "rotten/outpost_2";
                            offsetY = 20;
                        } else if (randI == 3) {
                            outpostType = "rotten/outpost_3";
                            offsetY = 12;
                        }
                        BlockPos posToo = new BlockPos(posI.getX() - 6, y - offsetY, posI.getZ() - 6);
                        rotten_outpost.startBuilding(posToo, Rotation.NONE, outpostType);
                        this.createInterestStructure(worldIn, posToo, 16, 0, 10, 4);
                        this.createInterestStructure(worldIn, posToo, 0, 16, 10, 4);
                        this.createInterestStructure(worldIn, posToo, -16, 0, 10, 4);
                        this.createInterestStructure(worldIn, posToo, 0, -16, 10, 4);
                        if(worldIn.rand.nextInt(2) == 0) {
                            this.createInterestStructure(worldIn, posToo, 16, 16, 10, 4);
                            this.createInterestStructure(worldIn, posToo, -16, -16, 10, 4);
                        }
                    }
                    this.updateBoundingBox();

                    if (this.isSizeableStructure()) {

                        break;
                    }
                }
            }
        }

        private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
        {
            int currentY = max;

            while(currentY >= min)
            {
                if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock() && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES
                        && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES2 && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG2
                        && world.getBlockState(pos.add(0, currentY, 0)) != Blocks.WATER.getDefaultState()) {
                    return currentY;
                }

                currentY--;
            }

            return 0;
        }

        private void createInterestStructure(World worldIn, BlockPos posToo, int xDirection, int zDirection, int safeDistance, int randomRadius) {
            String[] poiTypes = {"rotten/poi_1","rotten/poi_2","rotten/poi_3","rotten/poi_4","rotten/poi_5",
                    "rotten/poi_6","rotten/poi_7","rotten/poi_8","rotten/poi_9","rotten/poi_10",
                    "rotten/poi_11","rotten/poi_12"};
            BlockPos pos_1 = findInterestPosition(worldIn, posToo, xDirection, zDirection, safeDistance, randomRadius);
            if(pos_1 != null) {
                int y1 = getSurfaceHeight(worldIn, new BlockPos(pos_1.getX(), 0, pos_1.getZ()), WorldConfig.overworld_outposts_min_y, WorldConfig.overworld_outposts_max_y);
                if(y1 != 0) {
                    BlockPos posSet = new BlockPos(pos_1.getX() - 3, y1 - 2, pos_1.getZ() - 3);
                    OutpostSidePiece small_structure = new OutpostSidePiece(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    small_structure.startBuilding(posSet, Rotation.NONE, ModRand.choice(poiTypes));
                }
            }
        }

        private BlockPos findInterestPosition(World world, BlockPos origin, int directionX, int directionZ, int safeDistance, int rand) {
            for(int i = 0; i < 10; i++) {
                BlockPos randPos = new BlockPos(origin.getX() + directionX + ModRand.range(1, rand), origin.getY(), origin.getZ() + directionZ  + ModRand.range(1, rand));
                // we want the random determind distance to be greater than the origin structure
                if(Math.abs(randPos.getX() - origin.getX()) > safeDistance || Math.abs(randPos.getZ() - origin.getZ()) > safeDistance) {
                    System.out.println("Found position at " + randPos);
                    return randPos;
                }
            }
            System.out.println("Failed to find position");
            return null;
        }

        private boolean validBiomeType(BiomeDictionary.Type biomesAllowed, BlockPos pos, World world) {
            for(Biome biome : BiomeDictionary.getBiomes(biomesAllowed)) {
                if(biome != null) {
                    if (BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos), biomesAllowed)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }


        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() >= 2;
        }


    }

}
