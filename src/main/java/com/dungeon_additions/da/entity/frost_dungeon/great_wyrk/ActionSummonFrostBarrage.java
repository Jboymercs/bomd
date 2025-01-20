package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionSummonFrostBarrage implements IAction {


    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        for(int i = 0; i < 66; i += 3) {
            actor.addEvent(()-> {
                ProjectileFrostBullet bullet = new ProjectileFrostBullet(actor.world, actor, actor.getAttack() * 0.6F);
                Vec3d posRand = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(ModRand.range(-3, 3), 4.5 + ModRand.getFloat(1.5F), ModRand.range(-3, 3))));
                bullet.setPosition(posRand.x, posRand.y, posRand.z);
                actor.world.spawnEntity(bullet);
                bullet.setTravelRange(40);
                actor.playSound(SoundsHandler.BIG_WYRK_BARRAGE, 1.5f, 1.0f / (new Random().nextFloat() * 0.4F + 0.4f));

                for(int b = 0; b < 10; b++) {
                    actor.addEvent(()-> ModUtils.setEntityPosition(bullet, posRand), b);
                }

                actor.addEvent(()-> {
                    Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));
                    Vec3d fromTargetTooActor = posRand.subtract(targetPos);
                    Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                    Vec3d lineStart = targetPos.subtract(lineDir);
                    Vec3d lineEnd = targetPos.add(lineDir);
                    float speed = (float) 1.4;
                    ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, c) -> {
                        ModUtils.throwProjectileNoSpawn(pos,bullet,0F, speed);
                    });
                }, 11);
            }, i);
        }
    }
}
