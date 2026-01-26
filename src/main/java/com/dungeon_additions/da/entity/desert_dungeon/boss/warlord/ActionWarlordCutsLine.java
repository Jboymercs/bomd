package com.dungeon_additions.da.entity.desert_dungeon.boss.warlord;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileThousandCuts;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntitySummonedMace;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionWarlordCutsLine implements IAction {

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(3));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 1);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(6));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 5);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(9));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 9);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(12));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 13);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(15));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 17);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(18));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 21);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(21));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 25);

        actor.addEvent(()-> {
            Vec3d lineDir = targetPos.subtract(actor.getPositionVector()).normalize();
            Vec3d lineEnd = actor.getPositionVector().add(lineDir.scale(24));

            ModUtils.lineCallback(lineEnd, lineEnd, 1, (pos, i) -> {
                doCutsSummon(pos, actor);

            });
        }, 29);
    }

    private void doCutsSummon(Vec3d centerPos, EntityAbstractBase actor) {
        ProjectileThousandCuts cuts_projectile = new ProjectileThousandCuts(actor.world, actor, actor.getAttack());
        int y = getSurfaceHeight(actor.world, new BlockPos(centerPos.x, 0, centerPos.z), (int) actor.posY - 4, (int) actor.posY + 2);
        cuts_projectile.setPosition(centerPos.x, y + 3, centerPos.z);
        actor.world.spawnEntity(cuts_projectile);
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
