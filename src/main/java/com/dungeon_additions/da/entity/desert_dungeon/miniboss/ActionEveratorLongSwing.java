package com.dungeon_additions.da.entity.desert_dungeon.miniboss;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionEveratorLongSwing implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 0)));

        for(int t = 1; t < 5; t++ ) {
            ModUtils.circleCallback(t, (4 * t), (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                tile.setPosition(pos.x, pos.y, pos.z);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                Block blockToo = actor.world.getBlockState(posToo).getBlock();
                if(actor.world.getBlockState(posToo).isFullBlock()) {
                    tile.setBlock(blockToo, 0);
                } else {
                    tile.setBlock(Blocks.SAND, 0);
                }
                actor.world.spawnEntity(tile);

            });
        }

        actor.addEvent(()-> {
            for(int t = 5; t < 12; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    } else {
                        tile.setBlock(Blocks.SAND, 0);
                    }
                    actor.world.spawnEntity(tile);

                });
            }
        }, 20);
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
