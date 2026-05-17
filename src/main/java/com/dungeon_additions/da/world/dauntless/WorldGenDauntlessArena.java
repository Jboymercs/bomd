package com.dungeon_additions.da.world.dauntless;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
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

public class WorldGenDauntlessArena extends WorldGenerator  {

    private int spacing;
    private int separation;


    public WorldGenDauntlessArena() {
        this.spacing = WorldConfig.dauntless_spacing;
        this.separation = 16;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {

        //Checks to see if all biomes are valid in this region before selecting and spawning the structure
        if(canSpawnStructureAtPos(world, position.getX() >> 4, position.getZ() >> 4)) {
            getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - 50, position.getZ() - 50, position.getX() + 50, position.getZ() + 50));
            return true;
        }

        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenDauntlessArena.Start(world, rand, chunkX, chunkZ);
    }

    protected boolean canSpawnStructureAtPos(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing - 1;
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random =  world.setRandomSeed(k, l, 80702343);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypes()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> dauntlessBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(dauntlessBiomeTypes == null) {
            dauntlessBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_whitelist_dauntless) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) dauntlessBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return dauntlessBiomeTypes;
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
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand = random.nextInt(Rotation.values().length);

            BlockPos posI = new BlockPos(chunkX * 16 + 4, 0, chunkZ * 16 + 4);
            BlockPos ordinal_2 = new BlockPos(posI.getX() + 15, 0, posI.getZ() + 15);
            int y = getSurfaceHeight(worldIn, posI, WorldConfig.dauntless_min_y, WorldConfig.dauntless_max_y);
            int y2 = getSurfaceHeight(worldIn, ordinal_2, WorldConfig.dauntless_min_y, WorldConfig.dauntless_max_y);


            //basically this is to help bring two points to a more common ground
            int diff = y - y2;
            int regulatedY = diff/2;
            if(y != 0 && y2 != 0) {
                if(y2 < y + 2 || y2 > y - 2) {
                    for (int i = 0; i < 4; i++) {
                        components.clear();

                        BlockPos blockpos = posI.add(-10, y - 2 + Math.abs(regulatedY), -10);
                            DauntlessArena arena = new DauntlessArena(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                            arena.startBuilding(blockpos, Rotation.NONE, "arena");



                        this.updateBoundingBox();

                        this.valid = true;
                        if (this.isSizeableStructure()) {

                            break;
                        }
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
            return components.size() > 1;
        }


    }

}
