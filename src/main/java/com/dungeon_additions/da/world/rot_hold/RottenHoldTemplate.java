package com.dungeon_additions.da.world.rot_hold;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.EntityNetherAbberrant;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
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

public class RottenHoldTemplate extends ModStructureTemplate {


    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "rot_hold");
    private static final ResourceLocation LOOT_KEY = new ResourceLocation(ModReference.MOD_ID, "rot_hold_key");
    public RottenHoldTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public RottenHoldTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //mobs
        if(function.startsWith("set_mob")) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_MOSS.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityRotKnight.class), 1)
                        },
                        new int[]{1},
                        1,
                        30);
            }
        } else if (function.startsWith("mob")) {
            //both shield and other variant
            if(generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_MOSS.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityRotKnight.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityRotKnightRapier.class), 1)
                            },
                            new int[]{1, 2},
                            1,
                            20);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }

        //loot
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

    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.rot_hold_mob_spawns) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.rot_hold_chest_spawns) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "rot_hold";
    }
}
