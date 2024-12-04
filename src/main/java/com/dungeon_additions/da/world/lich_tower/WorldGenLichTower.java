package com.dungeon_additions.da.world.lich_tower;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.world.rot_hold.RottenHold;
import com.dungeon_additions.da.world.rot_hold.WorldGenRotHold;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenLichTower extends WorldGenerator {

    private int spacing = 0;

    public WorldGenLichTower() {

    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock() && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES
                    && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LEAVES2 && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG && world.getBlockState(pos.add(0, currentY, 0)).getBlock() != Blocks.LOG2 &&
                    world.isAirBlock(pos.add(0, currentY + 1, 0)) && world.getBlockState(pos.add(0, currentY, 0)) != Blocks.WATER.getDefaultState()) {
                return currentY;
            }

            currentY--;
        }
        //returns 0 if out of bounds
        return 0;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;
        return new WorldGenLichTower.Start(world, rand, chunkX, chunkZ);
    }


    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        BlockPos pos_edge = position.add(9, 0, 9);
        int yVar1 = getSurfaceHeight(world, position, WorldConfig.lich_tower_min_y, WorldConfig.lich_tower_max_y);
        int yVar2 = getSurfaceHeight(world, pos_edge, WorldConfig.lich_tower_min_y, WorldConfig.lich_tower_max_y);
        //Make sure we're not spawning in the void or are out of bounds for our spawning
        if(yVar1 != 0 && yVar2 != 0) {
            BlockPos ordinal_1 = new BlockPos(position.getX(), yVar1, position.getZ());
            BlockPos ordinal_2 = new BlockPos(pos_edge.getX(), yVar2, pos_edge.getZ());
            if (spacing > WorldConfig.lich_tower_spacing * 18 && world.getBlockState(ordinal_1).isFullBlock() && world.getBlockState(ordinal_2).isFullBlock() &&
            yVar2 < yVar1 + 2 || spacing > WorldConfig.lich_tower_spacing * 18 && world.getBlockState(ordinal_1).isFullBlock() && world.getBlockState(ordinal_2).isFullBlock() &&
            yVar2 > yVar1 - 2) {
                getStructureStart(world, ordinal_1.getX() >> 4, ordinal_2.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(ordinal_1.getX() - 50, ordinal_1.getZ() - 50, ordinal_1.getX() + 50, ordinal_1.getZ() + 50));
                return true;
            }
        }
        spacing++;
        return false;
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

            int y = getSurfaceHeight(worldIn, posI, WorldConfig.lich_tower_min_y, WorldConfig.lich_tower_max_y);

            if(y != 0) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();

                    BlockPos blockpos = posI.add(0, y - 9, 0);


                    if(validBiomeType(BiomeDictionary.Type.COLD, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SNOWY, blockpos, worldIn)) {
                        LichTowerFrozen tower_cold = new LichTowerFrozen(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        tower_cold.startTower(blockpos, Rotation.NONE);
                    } else if (validBiomeType(BiomeDictionary.Type.HOT, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SANDY, blockpos, worldIn)) {
                        LichTowerHot tower_hot = new LichTowerHot(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        tower_hot.startTower(blockpos, Rotation.NONE);
                    }
                    else if(validBiomeType(BiomeDictionary.Type.FOREST, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.PLAINS, blockpos, worldIn) ||
                    validBiomeType(BiomeDictionary.Type.SWAMP, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.JUNGLE, blockpos, worldIn)) {
                        LichTowerForest tower_forest = new LichTowerForest(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        tower_forest.startTower(blockpos, Rotation.NONE);
                    } else {
                        // maybe a base variant non biome influenced
                        LichTowerBase tower_base = new LichTowerBase(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        tower_base.startTower(blockpos, Rotation.NONE);
                        System.out.println("No biome types found in selection, generating a base lich tower");
                    }


                    this.updateBoundingBox();

                    this.valid = true;
                    if (this.isSizeableStructure()) {

                        break;
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
