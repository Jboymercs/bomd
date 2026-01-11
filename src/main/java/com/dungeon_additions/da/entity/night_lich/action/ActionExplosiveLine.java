package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionExplosiveLine implements IActionLich{
    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionEyes(1);
        Vec3d fromTargetToActor = actor.getPositionVector().subtract(targetPos);
        Vec3d lineDirection = ModUtils.rotateVector2(
                        fromTargetToActor.crossProduct(ModUtils.Y_AXIS),
                        fromTargetToActor,
                        135)
                .normalize()
                .scale(12);

        Vec3d lineStart = targetPos.subtract(lineDirection);
        Vec3d lineEnd = targetPos.add(lineDirection);

        //creates a 3 point line
        ModUtils.lineCallback(lineStart, lineEnd, 3, (pos, i) -> {
            Vec3d posParticle = new Vec3d(pos.x, actor.posY, pos.z);
            Vec3d posSet = actor.getPositionVector().subtract(posParticle).normalize();
            Vec3d adjusted = posParticle.add(posSet.scale(-6));

            Vec3d lineDir = actor.getPositionVector().subtract(adjusted);
            Vec3d lineStart2 = adjusted.subtract(lineDir);
            Vec3d lineEnd2 = adjusted.add(lineDir);

            //spawns particles
            ModUtils.lineCallback(lineStart2, lineEnd2, 50, (posV, j) -> {
                Main.proxy.spawnParticle(17, posV.x, posV.y, posV.z, 0, 0, 0, 60);
            });

            //spawns explosive projectiles
            ModUtils.lineCallback(lineStart2, lineEnd2, 12, (posV, j) -> {
                actor.addEvent(()-> {
                    EntityDelayedExplosion explosion = new EntityDelayedExplosion(actor.world, actor, actor.getAttack(), false, false);
                    int y = getSurfaceHeight(actor.world, new BlockPos(posV.x, 0, posV.z), (int) posV.y - 10, (int) posV.y + 10);
                    explosion.setPosition(posV.x, y + 2, posV.z);
                    actor.world.spawnEntity(explosion);
                }, j * 3);
            });
        });

    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

}
