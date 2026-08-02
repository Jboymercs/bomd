package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionRotLineAOE implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
            this.createRotLines(target, actor);
    }


    private void createRotLines(EntityLivingBase target, EntityAbstractBase actor) {
        Vec3d targetPos = target.getPositionEyes(1);
        Vec3d fromTargetToActor = actor.getPositionVector().subtract(targetPos);
        Vec3d lineDirection = ModUtils.rotateVector2(
                        fromTargetToActor.crossProduct(ModUtils.Y_AXIS),
                        fromTargetToActor,
                        135)
                .normalize()
                .scale(4);

        Vec3d lineStart = targetPos.subtract(lineDirection);
        Vec3d lineEnd = targetPos.add(lineDirection);

        //creates a 3 point line
        ModUtils.lineCallback(lineStart, lineEnd, 5, (pos, i) -> {
            Vec3d posParticle = new Vec3d(pos.x, actor.posY, pos.z);
            Vec3d posSet = actor.getPositionVector().subtract(posParticle).normalize();
            Vec3d adjusted = posParticle.add(posSet.scale(-2));

            Vec3d lineDir = actor.getPositionVector().subtract(adjusted);
            Vec3d lineStart2 = adjusted.subtract(lineDir);
            Vec3d lineEnd2 = adjusted.add(lineDir);

            //spawns particles
         //   ModUtils.lineCallback(lineStart2, lineEnd2, 20, (posV, j) -> {
         //       Main.proxy.spawnParticle(17, posV.x, posV.y + 1, posV.z, 0, 0, 0, 60);
         //   });

            //spawns explosive projectiles
            ModUtils.lineCallback(lineStart2, lineEnd2, (int)(actor.getDistance(target) * 2), (posV, j) -> {
                actor.addEvent(()-> {
                    EntityRotSpike arena = new EntityRotSpike(actor.world);
                    int y = getSurfaceHeight(actor.world, new BlockPos(posV.x, 0, posV.z), (int) posV.y - 3, (int) posV.y + 5);
                    arena.setPosition(posV.x, y + 1, posV.z);
                    actor.world.spawnEntity(arena);
                }, j );
            });
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
