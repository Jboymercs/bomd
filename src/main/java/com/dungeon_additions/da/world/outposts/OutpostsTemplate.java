package com.dungeon_additions.da.world.outposts;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.logic.MobSpawnerLogic;
import com.dungeon_additions.da.entity.tileEntity.tileEntityMobSpawner;
import com.dungeon_additions.da.entity.void_dungeon.EntityEnderphrite;
import com.dungeon_additions.da.entity.void_dungeon.EntityEnderphriteGauntlet;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiant;
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

public class OutpostsTemplate extends ModStructureTemplate {

    private static final ResourceLocation END_LOOT = new ResourceLocation(ModReference.MOD_ID, "end_outpost");
    private static final ResourceLocation END_LOOT_TREASURE = new ResourceLocation(ModReference.MOD_ID, "end_outpost_treasure");
    private static final ResourceLocation END_LOOT_TREASURE_NC = new ResourceLocation(ModReference.MOD_ID, "end_outpost_treasure_nc");

    public OutpostsTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    public OutpostsTemplate() {

    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //end
        if(function.startsWith("e_mob")) {
            if(generateMobSpawnEnd()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_END.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityVoidiant.class), 1)
                            },
                            new int[]{1},
                            1,
                            40);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if(function.startsWith("e_elite_mob")) {
            if(generateEliteMobSpawnEnd()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_END.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderphrite.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderphriteGauntlet.class), 1)
                            },
                            new int[]{1, 1},
                            1,
                            20);
                }
            } else {
                world.setBlockToAir(pos);
            }
        } else if (function.startsWith("e_chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawnEnd() && sbb.isVecInside(blockPos)) {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(END_LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        } else if (function.startsWith("e_treasure")) {
            BlockPos blockPos = pos.down();
            if(sbb.isVecInside(blockPos)) {
                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    if(WorldConfig.coins_spawn_in_chests) {
                        chest.setLootTable(END_LOOT_TREASURE, rand.nextLong());
                    } else {
                        chest.setLootTable(END_LOOT_TREASURE_NC, rand.nextLong());
                    }
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }
    }


    public boolean generateMobSpawnEnd() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 4) {
            return false;
        }
        return true;
    }

    public boolean generateEliteMobSpawnEnd() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 1) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawnEnd() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 6) {
            return false;
        }
        return true;
    }


    @Override
    public String templateLocation() {
        return "outpost";
    }
}
