package com.dungeon_additions.da.world.frozen_castle;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityEliteDraugr;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnight;
import com.dungeon_additions.da.entity.rot_knights.EntityRotKnightRapier;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.ModStructureTemplate;
import com.dungeon_additions.da.world.WorldGenStructure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class FrozenCastleTemplate extends ModStructureTemplate {


    private WorldGenCenterPiece[] structure_piece = {new WorldGenCenterPiece("piece_1"),new WorldGenCenterPiece("piece_2"),new WorldGenCenterPiece("piece_3"),
            new WorldGenCenterPiece("piece_4"),new WorldGenCenterPiece("piece_5")};

    private WorldGenCenterPiece mini_boss = new WorldGenCenterPiece("piece_7");
    public FrozenCastleTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public FrozenCastleTemplate() {

    }

    private boolean spawnedMini_boss = false;

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {


        //mobs
        if(function.startsWith("mob")) {
            //both shield and other variant
            if(generateMobSpawn()) {
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
        } else if (function.startsWith("big_mob_set")) {
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
        }

        //loot
        if(function.startsWith("chest")) {

        } else if (function.startsWith("secret_chest")) {

        } else if (function.startsWith("key_chest")) {

        }

        //function blocks
        if(function.startsWith("core")) {

        } else if (function.startsWith("center_piece")) {
                WorldGenCenterPiece piece = ModRand.choice(structure_piece);
                piece.generate(world, rand, pos.add(-4, 0, -4));
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
    @Override
    public String templateLocation() {
        return "frozen_castle";
    }
}
