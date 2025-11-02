package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionPlayerFallSlam implements IActionPlayer{

    private int rings;

    public ActionPlayerFallSlam(int rings) {
        this.rings = rings;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d savedPos = actor.getPositionVector();
        if(rings > 7) {
            for(int t = 1; t < 6; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityIcicleSpike tile = new EntityIcicleSpike(actor.world, actor, 10);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                    BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);
                });

            }
        } else {
            for(int t = 1; t < rings; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityIcicleSpike tile = new EntityIcicleSpike(actor.world, actor, 10);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                    BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);
                });

            }
        }
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.isBlockFullCube(pos.add(0, currentY, 0))) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
