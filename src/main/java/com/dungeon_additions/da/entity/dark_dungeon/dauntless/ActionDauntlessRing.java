package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ActionDauntlessRing implements IActionDauntless{
    @Override
    public void performAction(EntityDauntless actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();
        actor.addEvent(()-> {
            ModUtils.circleCallback(6, 18, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1 );
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
                  });
        }, 5);
        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 14, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1 );
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 10);
        actor.addEvent(()-> {
            ModUtils.circleCallback(4, 10, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1 );
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 15);
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
