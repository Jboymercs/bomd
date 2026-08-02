package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionChevStomp implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        ModUtils.circleCallback(1, 4, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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
        ModUtils.circleCallback(2, 8, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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
        ModUtils.circleCallback(3, 12, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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

        ModUtils.circleCallback(4, 16, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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

        ModUtils.circleCallback(5, 20, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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

        ModUtils.circleCallback(6, 24, (pos) -> {
            pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
            EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
            tile.setPosition(pos.x, pos.y, pos.z);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
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

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
