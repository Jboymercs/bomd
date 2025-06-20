package com.dungeon_additions.da.world.forgotten_temple;

import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.desert_dungeon.EntityScutterBeetle;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugr;
import com.dungeon_additions.da.entity.frost_dungeon.draugr.EntityDraugrRanger;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModEntities;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.world.ModStructureTemplate;
import com.dungeon_additions.da.world.forgotten_temple.parts.WorldGenTempleTop;
import com.dungeon_additions.da.world.nether_arena.WorldGenCenterPiece;
import com.dungeon_additions.da.world.nether_arena.WorldGenChain;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class ForgottenTempleTemplate extends ModStructureTemplate {

    private static final WorldGenTempleTop temple_top = new WorldGenTempleTop("temple_top");
    public ForgottenTempleTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public ForgottenTempleTemplate() {

    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        if(function.startsWith("temple_top")) {
            temple_top.generate(world, rand, pos);
        }

        if(function.startsWith("surface_mob")) {
            if(generateBeetleSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_FORGOTTEN_TEMPLE.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityScutterBeetle.class), 1)
                            },
                            new int[]{1},
                            ModRand.range(1, 3),
                            20);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
    }

    public boolean generateBeetleSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 6) {
            return false;
        }
        return true;
    }
    @Override
    public String templateLocation() {
        return "forgotten_temple";
    }
}
