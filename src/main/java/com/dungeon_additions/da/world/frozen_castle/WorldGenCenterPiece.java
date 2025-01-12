package com.dungeon_additions.da.world.frozen_castle;

import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenCenterPiece extends WorldGenStructure {
    public WorldGenCenterPiece(String structureName) {
        super("frozen_castle/" + structureName);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        //loot
        if (function.startsWith("chest")) {

        }

        //mobs
        if (function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_MOSS.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDraugr.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDraugrRanger.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityWyrk.class), 1)
                            },
                            new int[]{3, 3, 1},
                            ModRand.range(2, 5),
                            20);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("big_mob")) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_MOSS.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEliteDraugr.class), 1)
                        },
                        new int[]{1},
                        1,
                        16);
            }
        } else if (function.startsWith("big_mob_double")) {
            if(generateBigMob()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_MOSS.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEliteDraugr.class), 1)
                            },
                            new int[]{1},
                            1,
                            16);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }

    }


    public boolean generateBigMob() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 3) {
            return false;
        }
        return true;
    }
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 7) {
            return false;
        }
        return true;
    }
}
