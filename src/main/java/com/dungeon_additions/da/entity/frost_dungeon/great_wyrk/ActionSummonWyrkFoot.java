package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrkFoot;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionSummonWyrkFoot implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        EntityWyrkFoot foot = new EntityWyrkFoot(actor.world);

        Vec3d setPos1 = target.getPositionVector().add(ModRand.range(1, 3) + 1, 0, ModRand.range(1, 3) + 1);
        int y = getSurfaceHeight(actor.world, new BlockPos(setPos1.x, 0, setPos1.z), (int) target.posY - 3, (int) target.posY + 2);
        foot.setPosition(setPos1.x, y + 1, setPos1.z);
        actor.world.spawnEntity(foot);


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
