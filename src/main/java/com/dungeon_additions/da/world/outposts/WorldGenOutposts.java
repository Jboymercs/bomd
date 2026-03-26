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

import java.util.List;
import java.util.Random;

public class WorldGenOutposts extends WorldGenerator {

    private int spacing;
    private int spacing_nether;
    private int spacing_end;
    private int separation;

    public WorldGenOutposts() {
        this.separation = 16;
        this.spacing_end = WorldConfig.end_outposts_spacing;
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

            BlockPos posI = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);

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
