package com.dungeon_additions.da.world.blossom;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.blossom.EntityVoidBlossom;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.entity.util.EntityBossSpawner;
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

public class BlossomTemplate extends ModStructureTemplate {

    protected WorldGenStructure top = new WorldGenBlossomTop("blossom/blossom_top_" + ModRand.range(1, 4));
    public BlossomTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public BlossomTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        if(function.startsWith("")) {
            world.setBlockToAir(pos);
        }


        if(function.startsWith("boss")) {
            //Spawns a temp entity that will spawn the boss when a player is close enough
            EntityBossSpawner blossom = new EntityBossSpawner(world, 1, 30);
            blossom.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(blossom);
            //Generates the top part of the Blossom Cave, that sticks out in the surface
            if(WorldConfig.top_part_spawn) {
                BlockPos posToo = new BlockPos(pos.getX(), 0, pos.getZ());
                top.generate(world, rand, posToo);
            }
          //  world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
          //  TileEntity tileentity = world.getTileEntity(pos);
         //   if (tileentity instanceof tileEntityMobSpawner) {
          //      ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
           //             new MobSpawnerLogic.MobSpawnData[]{
          //                     new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityVoidBlossom.class), 1)
          //              },
         //               new int[]{1},
         //               1,
         //               35);
         //   }
        }
    }
    @Override
    public String templateLocation() {
        return "blossom";
    }
}
