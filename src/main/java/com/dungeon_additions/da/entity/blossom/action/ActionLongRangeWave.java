package com.dungeon_additions.da.entity.blossom.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionLongRangeWave implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(14, 64, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(15, 68, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(16, 72, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(17, 76, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(18, 76, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(19, 80, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(20, 84, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(21, 84, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(22, 90, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 7);
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
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
