package com.dungeon_additions.da.world.obsidilith_arena;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.util.DALogger;
import com.google.common.collect.Lists;
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

public class WorldGenObsidilithArena extends WorldGenerator {

    private int spacing;
    private int separation;

    public WorldGenObsidilithArena() {
        this.spacing = WorldConfig.obsidilith_arena_spacing;
        this.separation = 16;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        //Checks to see if all biomes are valid in this region before selecting and spawning the structure
        if(canSpawnStructureAtPos(world, position.getX() >> 4, position.getZ() >> 4)) {
            getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - 250, position.getZ() - 250, position.getX() + 250, position.getZ() + 250));
            return true;
        }

        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenObsidilithArena.Start(world, rand, chunkX, chunkZ);
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
        Random random =  world.setRandomSeed(k, l, 20387994);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHereObsidilithArena(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHereObsidilithArena(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypesObsidilithArena()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> highCityBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypesObsidilithArena() {
        if(highCityBiomeTypes == null) {
            highCityBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.obsdilith_arena_whitelist) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) highCityBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return highCityBiomeTypes;
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

            BlockPos posI = new BlockPos(chunkX * 16 + 4, WorldConfig.obsidilith_y_height, chunkZ * 16 + 4);


            //basically this is to help bring two points to a more common ground
            for (int i = 0; i < 4; i++) {
                Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                components.clear();

                ObsidilithArena obsidilithArena = new ObsidilithArena(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                obsidilithArena.startArena(posI, rotation);
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
            return components.size() >= 2;
        }


    }

}
