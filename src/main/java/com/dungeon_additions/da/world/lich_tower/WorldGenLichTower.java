package com.dungeon_additions.da.world.lich_tower;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
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

public class WorldGenLichTower extends WorldGenerator {

    private int spacing;
    private int separation;

    private static List<Biome> spawnBiomesLichTower;
    public WorldGenLichTower() {
    this.spacing = WorldConfig.lich_tower_spacing;
    this.separation = 16;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenLichTower.Start(world, rand, chunkX, chunkZ);
    }


    @Override
    public boolean generate(World world, Random rand, BlockPos position) {

        //Checks to see if all biomes are valid in this region before selecting and spawning the structure
        if(canSpawnStructureAtPos(world, position.getX() >> 4, position.getZ() >> 4)) {
                getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - 50, position.getZ() - 50, position.getX() + 50, position.getZ() + 50));
            System.out.println("Generated Lich Tower at" + position);
                return true;
            }

        return false;
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
        Random random =  world.setRandomSeed(k, l, 10387312);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return WorldConfig.night_lich_is_blacklist == (world.provider.getBiomeForCoords(pos) != getSpawnBiomesLichTower().iterator());
        } else {

            return false;
        }
    }

    public static List<Biome> getSpawnBiomesLichTower() {
        if (spawnBiomesLichTower == null) {
            spawnBiomesLichTower = Lists.newArrayList();
            for (String str : WorldConfig.biome_allowed_lich) {
                try {
                    Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                    if (biome != null) spawnBiomesLichTower.add(biome);
                    else DALogger.logError("Biome " + str + " is not registered", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid registry name", e);
                }
            }
        }
        return spawnBiomesLichTower;
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
            BlockPos ordinal_2 = new BlockPos(posI.getX() + 9, 0, posI.getZ() + 9);
            int y = getSurfaceHeight(worldIn, posI, WorldConfig.lich_tower_min_y, WorldConfig.lich_tower_max_y);
            int y2 = getSurfaceHeight(worldIn, ordinal_2, WorldConfig.lich_tower_min_y, WorldConfig.lich_tower_max_y);


                //basically this is to help bring two points to a more common ground
                int diff = y - y2;
                int regulatedY = diff/2;
                if(y != 0 && y2 != 0) {
                if(y2 < y + 2 || y2 > y - 2) {
                    for (int i = 0; i < 4; i++) {
                        Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                        components.clear();

                        BlockPos blockpos = posI.add(0, y - 8 + regulatedY, 0);


                        if (validBiomeType(BiomeDictionary.Type.COLD, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SNOWY, blockpos, worldIn)) {
                            LichTowerFrozen tower_cold = new LichTowerFrozen(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                            tower_cold.startTower(blockpos, Rotation.NONE);
                        } else if (validBiomeType(BiomeDictionary.Type.HOT, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SANDY, blockpos, worldIn)) {
                            LichTowerHot tower_hot = new LichTowerHot(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                            tower_hot.startTower(blockpos, Rotation.NONE);
                        } else if (validBiomeType(BiomeDictionary.Type.FOREST, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.PLAINS, blockpos, worldIn) ||
                                validBiomeType(BiomeDictionary.Type.SWAMP, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.JUNGLE, blockpos, worldIn)) {
                            LichTowerForest tower_forest = new LichTowerForest(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                            tower_forest.startTower(blockpos, Rotation.NONE);
                        } else {
                            // maybe a base variant non biome influenced
                            LichTowerBase tower_base = new LichTowerBase(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                            tower_base.startTower(blockpos, Rotation.NONE);
                        }


                        this.updateBoundingBox();

                        this.valid = true;
                        if (this.isSizeableStructure()) {

                            break;
                        }
                    }

                }

            }

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
            return components.size() >= 5;
        }


    }
}
