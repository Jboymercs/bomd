package com.dungeon_additions.da.entity.void_dungeon.obsidilith_action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.void_dungeon.EntityBlueWave;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ActionRedWave implements IAction {


    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));
        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(5));

            ModUtils.lineCallback(lineStart, lineEnd, 2, (pos, i) -> {
                doRedRing(pos, actor);
            });
        }, 1);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(11));

            ModUtils.lineCallback(lineStart, lineEnd, 2, (pos, i) -> {
                    doRedRing(pos, actor);
            });
        }, 21);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(17));

            ModUtils.lineCallback(lineStart, lineEnd, 2, (pos, i) -> {
                    doRedRing(pos, actor);
            });
        }, 41);
    }

    private void doRedRing(Vec3d centerPos, EntityLivingBase actor) {

        ModUtils.circleCallback(0, 1, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityBlueWave spike = new EntityBlueWave(actor.world, false, true, false);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(1, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityBlueWave spike = new EntityBlueWave(actor.world, false, true, false);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(2, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityBlueWave spike = new EntityBlueWave(actor.world, false, true, false);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(3, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityBlueWave spike = new EntityBlueWave(actor.world, false, true, false);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(4, 16, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(centerPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityBlueWave spike = new EntityBlueWave(actor.world, false, true, false);
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
