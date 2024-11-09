package com.dungeon_additions.da.world.blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class WorldGenBlossomCave extends WorldGenerator {

    private int spacing = 0;

    public WorldGenBlossomCave() {

    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        BlockPos pos2 = pos.add(53, 0, 53);
        if(spacing/12 > WorldConfig.void_blossom_cave_weight && world.getBlockState(pos).isFullBlock() && world.getBlockState(pos2).isFullBlock()) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 150, pos.getZ() - 150, pos.getX() + 150, pos.getZ() + 150));
            return true;

        }
        spacing++;
        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;

        return new WorldGenBlossomCave.Start(world, rand, chunkX, chunkZ);
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


                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();

                    BlockPos blockpos = posI.add(0, new Random().nextInt(WorldConfig.y_height_random) + WorldConfig.y_height_static, 0);
                    BlossomCave stronghold = new BlossomCave(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    stronghold.startVault(blockpos, Rotation.NONE);
                    this.updateBoundingBox();

                    this.valid = true;
                    if (this.isSizeableStructure()) {

                        break;
                    }


            }

        }
        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() >= 1;
        }


    }
}
