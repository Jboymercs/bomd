package com.dungeon_additions.da.entity.desert_dungeon.boss.colossus;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySummonedMace;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionMaceWave implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(5));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                    doMaceSummon(pos, actor);

            });
        }, 1);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(11));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                    doMaceSummon(pos, actor);

            });
        }, 21);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineStart = actor.getPositionVector().add(lineDir);
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(17));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                    doMaceSummon(pos, actor);

            });
        }, 41);
    }

    private void doMaceSummon(Vec3d centerPos, EntityAbstractBase actor) {
        EntitySummonedMace mace = new EntitySummonedMace(actor.world, false, actor.getAttack(), actor);
        int y = getSurfaceHeight(actor.world, new BlockPos(centerPos.x, 0, centerPos.z), (int) actor.posY - 4, (int) actor.posY + 2);
        mace.setPosition(centerPos.x, y + 1, centerPos.z);
        actor.world.spawnEntity(mace);
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
