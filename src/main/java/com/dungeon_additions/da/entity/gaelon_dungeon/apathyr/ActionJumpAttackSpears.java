package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidclysmSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionJumpAttackSpears implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();


        ModUtils.circleCallback(1, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(2, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(3, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(4, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        actor.addEvent(()-> ModUtils.circleCallback(5, 25, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 4);

        actor.addEvent(()-> ModUtils.circleCallback(6, 36, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 8);

        actor.addEvent(()-> ModUtils.circleCallback(7, 25, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 12);

        actor.addEvent(()-> ModUtils.circleCallback(8, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 16);

        actor.addEvent(()-> ModUtils.circleCallback(9, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 20);

        actor.addEvent(()-> ModUtils.circleCallback(10, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 7, (int) actor.posY + 3);
            EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        }), 24);
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
