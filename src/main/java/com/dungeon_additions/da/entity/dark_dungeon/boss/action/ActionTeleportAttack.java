package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionTeleportAttack implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(3, 7, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 4);
            EntityGreatDeath spike = new EntityGreatDeath(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        EntityGreatDeath spike2 = new EntityGreatDeath(actor.world);
        spike2.setPosition(actor.posX, actor.posY, actor.posZ);
        actor.world.spawnEntity(spike2);


        if(ModRand.getFloat(1) <= 0.4) {
            actor.addEvent(()-> {
                ModUtils.circleCallback(10, 20, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 5, (int) actor.posY + 4);
                    EntityGreatDeath spike = new EntityGreatDeath(actor.world);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(spike);
                });
            }, 10);
        }
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
