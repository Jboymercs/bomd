package com.dungeon_additions.da.world.cults_domain;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.dark_dungeon.*;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.ModStructureTemplate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class CultsDomainTemplate extends ModStructureTemplate {

    private boolean enableMobs = false;
    public CultsDomainTemplate() {

    }
    public CultsDomainTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //mobs
        if(function.startsWith("mob")) {
            //both shield and other variant
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_DARK.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDarkAssassin.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDarkRoyal.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDarkSorcerer.class), 1)
                            },
                            new int[]{3, 3, 1},
                            1,
                            24);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }  else if(function.startsWith("elite_mob")) {
            //both shield and other variant
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_DARK.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDarkRoyal.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityDarkSorcerer.class), 1)
                            },
                            new int[]{5, 1},
                            1,
                            24);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }   else if(function.startsWith("boss")) {
            //boss spawner
            EntityRohsEye eye = new EntityRohsEye(world);
            eye.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.spawnEntity(eye);
            world.setBlockToAir(pos);
        } else if (function.startsWith("flying")) {
                if (ModRand.percentageOf(50)) {
                    EntityBrineCreep creep = new EntityBrineCreep(world);
                    creep.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                    world.spawnEntity(creep);
                    world.setBlockToAir(pos);
                } else {
                    world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_DARK.getDefaultState(), 2);
                    TileEntity tileentity = world.getTileEntity(pos);
                    if (tileentity instanceof tileEntityMobSpawner) {
                        ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                                new MobSpawnerLogic.MobSpawnData[]{
                                        new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBrineCreep.class), 1)
                                },
                                new int[]{1},
                                1,
                                36);
                    }
                }
        } else if (function.startsWith("shield_elite")) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_DARK.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEverchosen.class), 1)
                        },
                        new int[]{1},
                        1,
                        14);
            }
        } else if (function.startsWith("hammer_elite")) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_DARK.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEverchosenHammer.class), 1)
                        },
                        new int[]{1},
                        1,
                        14);
            }
        }

    }


    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 6) {
            return false;
        }
        return true;
    }

    public boolean generateLightMob() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 5) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "dark_dungeon";
    }
}
