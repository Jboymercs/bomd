package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionDauntlessBigAOE implements IActionDauntless{

    private boolean doLazers;

    public ActionDauntlessBigAOE(boolean doLazers) {
        this.doLazers = doLazers;
    }

    @Override
    public void performAction(EntityDauntless actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();

        for(int t = 1; t < 4; t++ ) {
            ModUtils.circleCallback(t, (4 * t), (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                tile.setPosition(pos.x, pos.y, pos.z);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                Block blockToo = actor.world.getBlockState(posToo).getBlock();
                if(actor.world.getBlockState(posToo).isFullBlock()) {
                    tile.setBlock(blockToo, 0);
                }else if (actor.world.getBlockState(posToo.down()).isFullBlock()) {
                    Block blockToo2 = actor.world.getBlockState(posToo.down()).getBlock();
                    tile.setBlock(blockToo2, 0);
                } else {
                    tile.setBlock(Blocks.STONE, 0);
                }
                actor.world.spawnEntity(tile);

            });
        }

        actor.addEvent(()-> {
            for(int t = 4; t < 8; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    }else if (actor.world.getBlockState(posToo.down()).isFullBlock()) {
                        Block blockToo2 = actor.world.getBlockState(posToo.down()).getBlock();
                        tile.setBlock(blockToo2, 0);
                    } else {
                        tile.setBlock(Blocks.STONE, 0);
                    }
                    actor.world.spawnEntity(tile);

                });
            }
        }, 10);

        if(doLazers) {
            //vertical lazers
            actor.addEvent(() -> {
                Vec3d relPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 0, 0)));
                ProjectileVerticalLazer lazer = new ProjectileVerticalLazer(actor.world, actor, actor.getAttack() * 0.8F, 0.04F, 0.5F);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 5);
            actor.addEvent(() -> {
                Vec3d relPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
                ProjectileVerticalLazer lazer = new ProjectileVerticalLazer(actor.world, actor, actor.getAttack() * 0.8F, 0.06F, 1F);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 5);
            actor.addEvent(() -> {
                Vec3d relPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
                ProjectileVerticalLazer lazer = new ProjectileVerticalLazer(actor.world, actor, actor.getAttack() * 0.8F, 0.08F, 1.5F);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 5);
            actor.addEvent(() -> {
                Vec3d relPos = savedPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 0)));
                ProjectileVerticalLazer lazer = new ProjectileVerticalLazer(actor.world, actor, actor.getAttack() * 0.8F, 0.1F, 2F);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 5);
        }

        actor.addEvent(()-> {
            for(int t = 8; t < 12; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    } else if (actor.world.getBlockState(posToo.down()).isFullBlock()) {
                        Block blockToo2 = actor.world.getBlockState(posToo.down()).getBlock();
                        tile.setBlock(blockToo2, 0);
                    }else {
                        tile.setBlock(Blocks.STONE, 0);
                    }
                    actor.world.spawnEntity(tile);

                });
            }
        }, 20);

        actor.addEvent(()-> {
            for(int t = 12; t < 16; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 3);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    }else if (actor.world.getBlockState(posToo.down()).isFullBlock()) {
                        Block blockToo2 = actor.world.getBlockState(posToo.down()).getBlock();
                        tile.setBlock(blockToo2, 0);
                    } else {
                        tile.setBlock(Blocks.STONE, 0);
                    }
                    actor.world.spawnEntity(tile);

                });
            }
        }, 30);

    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote ) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
