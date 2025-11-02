package com.dungeon_additions.da.world.mysterious_trader;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.integration.ModIntegration;
import com.dungeon_additions.da.util.DALogger;
import com.dungeon_additions.da.world.lich_tower.WorldGenLichTower;
import com.dungeon_additions.da.world.nether_arena.NetherArena;
import com.dungeon_additions.da.world.nether_arena.NetherArenaCrimson;
import com.dungeon_additions.da.world.nether_arena.NetherArenaNBDefault;
import com.dungeon_additions.da.world.nether_arena.NetherArenaWarped;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

public class WorldGenMysteriousTraderPost extends WorldGenerator {

    private int spacing;
    private int separation;

    public WorldGenMysteriousTraderPost() {
        this.separation = 16;
        this.spacing = WorldConfig.mysterious_trader_post_spacing;

    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        if(canSpawnStructureAtPos(world, pos.getX() >> 4, pos.getZ() >> 4)) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 300, pos.getZ() - 300, pos.getX() + 300, pos.getZ() + 300));
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
        Random random =  world.setRandomSeed(k, l, 98438247);
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

    private static List<BiomeDictionary.Type> lichTowerBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(lichTowerBiomeTypes == null) {
            lichTowerBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_blacklist_trader_outpost) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) lichTowerBiomeTypes.add(type);
                    else DALogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    DALogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return lichTowerBiomeTypes;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenMysteriousTraderPost.Start(world, rand, chunkX, chunkZ);
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
            int y = getSurfaceHeight(worldIn, posI, 30, 150);

            //ensures theres ground somewhere within a range
            if(y != 0) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();
                    BlockPos blockpos = posI.add(0, y, 0);
                    //Desert Variant
                    if(validBiomeType(BiomeDictionary.Type.HOT, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SANDY, blockpos, worldIn)) {
                        MysteriousTraderPostGeneric post = new MysteriousTraderPostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        post.startBuilding(blockpos.add(-4,-2,-4), Rotation.NONE, "trader_desert");
                        //plains
                    } else if(validBiomeType(BiomeDictionary.Type.PLAINS, blockpos, worldIn)) {
                        MysteriousTraderPostGeneric post = new MysteriousTraderPostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        post.startBuilding(blockpos.add(-2,-1,-2), Rotation.NONE, "trader_plains");
                        //jungle
                    } else if(validBiomeType(BiomeDictionary.Type.JUNGLE, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.SWAMP, blockpos, worldIn)) {
                        MysteriousTraderPostGeneric post = new MysteriousTraderPostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        post.startBuilding(blockpos.add(-5,-8,-4), Rotation.NONE, "trader_jungle");
                        //snow
                    } else if(validBiomeType(BiomeDictionary.Type.SNOWY, blockpos, worldIn) || validBiomeType(BiomeDictionary.Type.COLD, blockpos, worldIn)) {
                        MysteriousTraderPostGeneric post = new MysteriousTraderPostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        post.startBuilding(blockpos.add(-4,-1,-4), Rotation.NONE, "trader_snow");
                    }
                    //forest or generic variant
                   else {
                        MysteriousTraderPostGeneric post = new MysteriousTraderPostGeneric(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        post.startBuilding(blockpos.add(-8,-1,-4), Rotation.NONE, "trader_house");
                    }

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

        private boolean validBiomeType(BiomeDictionary.Type biomesAllowed, BlockPos pos, World world) {
            for(Biome biome : BiomeDictionary.getBiomes(biomesAllowed)) {
                if(biome != null) {
                    if (BiomeDictionary.hasType(world.getBiomeForCoordsBody(pos), biomesAllowed)) {
                        return true;
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
            return components.size() >= 1;
        }


    }

}
