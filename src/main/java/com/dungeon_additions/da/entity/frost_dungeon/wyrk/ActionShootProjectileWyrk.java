package com.dungeon_additions.da.entity.frost_dungeon.wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionShootProjectileWyrk implements IAction {


    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ProjectileFrostBullet missile = new ProjectileFrostBullet(actor.world, actor, (float) (actor.getAttack()));
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,2.3,0)));
        missile.setPosition(relPos.x, relPos.y, relPos.z);
        actor.world.spawnEntity(missile);
        missile.setTravelRange(40);

        for(int i = 0; i < 50; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,3.0,0)));
                ModUtils.setEntityPosition(missile, pos);
            }, i);
        }

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 1.4;

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
            });
        }, 51);

    }
}
