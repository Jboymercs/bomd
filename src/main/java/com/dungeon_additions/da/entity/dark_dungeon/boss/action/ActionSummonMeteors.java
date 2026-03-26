package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.boss.ProjectileBloodMeteor;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionSummonMeteors implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ProjectileBloodMeteor missile = new ProjectileBloodMeteor(actor.world, actor, (float) (actor.getAttack()));
        ProjectileBloodMeteor missile2 = new ProjectileBloodMeteor(actor.world, actor, (float) (actor.getAttack()));
        ProjectileBloodMeteor missile3 = new ProjectileBloodMeteor(actor.world, actor, (float) (actor.getAttack()));

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,6,0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,2,4)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,2,-4)));

        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile3.setPosition(relPos3.x, relPos3.y, relPos3.z);

        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile2);
        actor.world.spawnEntity(missile3);

        missile.setTravelRange(60);
        missile2.setTravelRange(60);
        missile3.setTravelRange(60);

        for(int i = 0; i < 30; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,6,0)));
                ModUtils.setEntityPosition(missile, pos);
            }, i);
        }

        for(int i = 0; i < 50; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,2,4)));
                ModUtils.setEntityPosition(missile2, pos);
            }, i);
        }

        for(int i = 0; i < 70; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,2,-4)));
                ModUtils.setEntityPosition(missile3, pos);
            }, i);
        }

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.3;
            Vec3d targetPos = target.getPositionEyes(1.0F);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
            });
        }, 30);

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.3;
            Vec3d targetPos = target.getPositionEyes(1.0F);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile2,0F, speed);
            });
        }, 50);

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.3;
            Vec3d targetPos = target.getPositionEyes(1.0F);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile3,0F, speed);
            });
        }, 70);
    }
}
