package com.dungeon_additions.da.world.high_city;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.sky_dungeon.*;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.world.ModStructureTemplate;
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

public class HighCityTemplate extends ModStructureTemplate {

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "high_court_city");
    private static final ResourceLocation LOOT_KEY = new ResourceLocation(ModReference.MOD_ID, "high_court_city_key");
    public HighCityTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public HighCityTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_HIGH_CITY.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityTridentGargoyle.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityMageGargoyle.class), 1)
                            },
                            new int[]{3,1},
                            ModRand.range(1, 4),
                            30);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("knight_ranged")) {
            if (generateKnightSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_HIGH_CITY.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityImperialSword.class), 1)
                            },
                            new int[]{1},
                            1,
                            35);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("knight")) {
            if (generateKnightSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_HIGH_CITY.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityImperialHalberd.class), 1)
                            },
                            new int[]{1},
                            1,
                            20);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("tornado")) {
            EntitySkyTornado tornado = new EntitySkyTornado(world);
            tornado.setPosition(pos.getX(), pos.getY(), pos.getZ());
            world.spawnEntity(tornado);
        }

        if(function.startsWith("chest")) {
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
        } else if (function.startsWith("key_chest")) {
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
        }
    }

    public boolean generateKnightSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.high_city_mob_spawns) {
            return false;
        }
        return true;
    }

    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.high_city_mob_spawns) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.high_city_chest_spawns) {
            return false;
        }
        return true;
    }


    @Override
    public String templateLocation() {
        return "high_city";
    }
}
