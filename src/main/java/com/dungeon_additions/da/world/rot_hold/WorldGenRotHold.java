package com.dungeon_additions.da.world.rot_hold;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.world.blossom.BlossomCave;
import com.dungeon_additions.da.world.blossom.WorldGenBlossomCave;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import org.lwjgl.Sys;

import java.util.Random;

public class WorldGenRotHold extends WorldGenerator {

    private int spacing = 0;

    public WorldGenRotHold() {

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

        return 0;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        BlockPos pos2 = pos.add(7, 0, 7);
        int yVar1 = getSurfaceHeight(world, pos.add(-6, 0, -6), WorldConfig.rot_hold_min_y, WorldConfig.rot_hold_max_y);
        int yVar2 = getSurfaceHeight(world, pos2, WorldConfig.rot_hold_min_y, WorldConfig.rot_hold_max_y);
        if(spacing > WorldConfig.rot_hold_spacing * 16 && world.getBlockState(pos.add(-6, 0, -6)).isFullBlock() && world.getBlockState(pos2).isFullBlock() && yVar1 != 0 && yVar2 != 0) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 150, pos.getZ() - 150, pos.getX() + 150, pos.getZ() + 150));
            return true;

        }
        System.out.println("Spacing is at" + spacing);
        spacing++;
        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;

        return new WorldGenRotHold.Start(world, rand, chunkX, chunkZ);
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

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);

            int y = getSurfaceHeight(worldIn, posI, WorldConfig.rot_hold_min_y, WorldConfig.rot_hold_max_y);

            if(y != 0) {
            for (int i = 0; i < 4; i++) {
                Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                components.clear();

                BlockPos blockpos = posI.add(0, y, 0);
                RottenHold stronghold = new RottenHold(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                stronghold.startHold(blockpos, Rotation.NONE);
                this.updateBoundingBox();

                this.valid = true;
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
            return components.size() >= 5;
        }


    }
}
