package com.dungeon_additions.da.world.nether_arena;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.flame_knight.EntityBareant;
import com.dungeon_additions.da.entity.flame_knight.EntityIncendium;
import com.dungeon_additions.da.entity.flame_knight.EntityPyre;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.integration.ModIntegration;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class NetherArenaTemplate extends ModStructureTemplate {

    private static final WorldGenChain chain = new WorldGenChain("chain");

    private static final WorldGenCenterPiece[] pieces = {new WorldGenCenterPiece("piece_1"),new WorldGenCenterPiece("piece_2"),new WorldGenCenterPiece("piece_3"),
            new WorldGenCenterPiece("piece_4"),new WorldGenCenterPiece("piece_5"),new WorldGenCenterPiece("piece_6"),
            new WorldGenCenterPiece("piece_7"),};
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "arena_chests");
    private static final ResourceLocation LOOT_KEY = new ResourceLocation(ModReference.MOD_ID, "arena_key_chest");
    public NetherArenaTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public NetherArenaTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //Generates chains at certain points of builds
        if(function.startsWith("chain")) {
            int t_height = findCeilingToConnect(world, pos.getX(), pos.getZ());
            if(t_height > pos.getY()) {
                //builds chains
                for(int v = pos.getY(); v <= t_height;v += 10) {
                    BlockPos modifiedPos = new BlockPos(pos.getX(), v, pos.getZ());
                    chain.generate(world, rand, modifiedPos);
                }
            }
        } else if (function.startsWith("piece")) {
            WorldGenCenterPiece pieceTooGenerate = ModRand.choice(pieces);
            pieceTooGenerate.generate(world, rand, pos);
        }


        //Boss Spawn
        if(function.startsWith("boss")) {
            EntityPyre pyre = new EntityPyre(world);
            pyre.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            world.setBlockToAir(pos);
            world.spawnEntity(pyre);
        }
        //Set mob spawns
        if(function.startsWith("set_mob")) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityNetherAbberrant.class), 1),
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityIncendium.class), 1),
                        },
                        new int[]{1,1},
                        1,
                        32);
            }
            //Chance Mob Spawns
        } else if (function.startsWith("mob")) {
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityNetherAbberrant.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityIncendium.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBareant.class), 1)
                            },
                            new int[]{1, 1, 1},
                            1,
                            32);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("spirit")) {
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityNetherAbberrant.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBareant.class), 1)
                            },
                            new int[]{1, 2},
                            2,
                            32);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }


        //Loot
        if(function.startsWith("key_chest")) {
            BlockPos blockPos = pos.down();
            if(sbb.isVecInside(blockPos)) {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT_KEY, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        } else if (function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn() && sbb.isVecInside(blockPos)) {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }
    }
    @Override
    public String templateLocation() {
            return "nether_arena";
    }

    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.burning_arena_mob_spawns) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.burning_arena_chest_spawns) {
            return false;
        }
        return true;
    }

    public static int findCeilingToConnect(World world, int x, int z)
    {
        int y = world.getActualHeight();
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.AIR;
        }

        return y;
    }
}
