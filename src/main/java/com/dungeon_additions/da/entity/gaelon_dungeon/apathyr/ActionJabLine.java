package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionJabLine implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();

        for(int i = 0; i < 15; i++) {
            int finalI = i;
            actor.addEvent(()-> {
                EntityApathyrSpear spear = new EntityApathyrSpear(actor.world);
                Vec3d relPos = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, 0)));
                int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 7, (int) actor.posY + 3);
                spear.setPosition(relPos.x, y + 1, relPos.z);
                actor.world.spawnEntity(spear);
            }, i);
        }

        for(int i = 0; i < 8; i++) {
            int finalI = i;
            actor.addEvent(()-> {
                EntityApathyrSpear spear = new EntityApathyrSpear(actor.world);
                Vec3d relPos = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, finalI)));
                int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 7, (int) actor.posY + 3);
                spear.setPosition(relPos.x, y + 1, relPos.z);
                actor.world.spawnEntity(spear);
            }, i);
        }

        for(int i = 0; i < 8; i++) {
            int finalI = i;
            actor.addEvent(()-> {
                EntityApathyrSpear spear = new EntityApathyrSpear(actor.world);
                Vec3d relPos = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(finalI, 0, -finalI)));
                int y = getSurfaceHeight(actor.world, new BlockPos(relPos.x, 0, relPos.z), (int) actor.posY - 7, (int) actor.posY + 3);
                spear.setPosition(relPos.x, y + 1, relPos.z);
                actor.world.spawnEntity(spear);
            }, i);
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
