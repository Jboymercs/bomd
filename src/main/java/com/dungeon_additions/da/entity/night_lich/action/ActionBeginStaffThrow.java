package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionBeginStaffThrow implements IActionLich{


    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        EntityLichStaffAOE staff_main = new EntityLichStaffAOE(actor.world, true);


        int y = getSurfaceHeight(actor.world, new BlockPos(target.posX, 0, target.posZ), (int) target.posY - 6, (int) target.posY + 3);
        staff_main.setPosition(target.posX, y, target.posZ);
        actor.world.spawnEntity(staff_main);
        Vec3d relPos = target.getPositionVector().add(ModUtils.getRelativeOffset(staff_main, new Vec3d(0, 1.2, 0)));
        Main.proxy.spawnParticle(21, relPos.x, target.posY, relPos.z, 0, 0, 0);

        actor.addEvent(()-> {
            EntityLichStaffAOE staff_1 = new EntityLichStaffAOE(actor.world, true);
            Vec3d posToo = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
            int y2 = getSurfaceHeight(actor.world, new BlockPos(posToo.x, 0, posToo.z), (int) posToo.x - 6, (int) posToo.z + 3);
            staff_1.setPosition(posToo.x, y2, posToo.z);
            actor.world.spawnEntity(staff_1);
        }, 30);

        actor.addEvent(()-> {
            EntityLichStaffAOE staff_2 = new EntityLichStaffAOE(actor.world, true);
            Vec3d posToo = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
            int y2 = getSurfaceHeight(actor.world, new BlockPos(posToo.x, 0, posToo.z), (int) posToo.x - 6, (int) posToo.z + 3);
            staff_2.setPosition(posToo.x, y2, posToo.z);
            actor.world.spawnEntity(staff_2);
        }, 60);

        actor.addEvent(()-> {
            EntityLichStaffAOE staff_3 = new EntityLichStaffAOE(actor.world, true);
            Vec3d posToo = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
            int y2 = getSurfaceHeight(actor.world, new BlockPos(posToo.x, 0, posToo.z), (int) posToo.x - 6, (int) posToo.z + 3);
            staff_3.setPosition(posToo.x, y2, posToo.z);
            actor.world.spawnEntity(staff_3);
        }, 90);
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
