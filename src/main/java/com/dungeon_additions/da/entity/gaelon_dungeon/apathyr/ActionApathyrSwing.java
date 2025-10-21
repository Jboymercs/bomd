package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionApathyrSwing implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();
        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 11, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 5);
                EntityApathyrSpear spike = new EntityApathyrSpear(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 11);
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
