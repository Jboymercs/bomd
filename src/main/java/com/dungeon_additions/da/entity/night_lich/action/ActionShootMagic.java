package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionShootMagic implements IActionLich {


    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        ProjectileMagicMissile missile = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileMagicMissile missile_2 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileMagicMissile missile_3 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack() * 0.8));
        ProjectileMagicMissile missile_4 = new ProjectileMagicMissile(actor.world, actor,(float) (actor.getAttack() * 0.8));
        ProjectileMagicMissile missile_5 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack() * 0.8));
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,3.0,0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.8,0.8)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.8,-0.8)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.6,-1.6)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.6,1.6)));
        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        missile_4.setPosition(relPos4.x, relPos4.y, relPos4.z);
        missile_5.setPosition(relPos5.x, relPos5.y, relPos5.z);

        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile_2);
        actor.world.spawnEntity(missile_3);
        actor.world.spawnEntity(missile_4);
        actor.world.spawnEntity(missile_5);
        missile.setTravelRange(40);
        missile_2.setTravelRange(40);
        missile_3.setTravelRange(40);
        missile_4.setTravelRange(40);
        missile_5.setTravelRange(40);
        for(int i = 0; i < 38; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,3.0,0)));
                ModUtils.setEntityPosition(missile, pos);
                }, i);

            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.8,0.8)));
                ModUtils.setEntityPosition(missile_2, pos);
                }, i);

            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.8,-0.8)));
                ModUtils.setEntityPosition(missile_3, pos);
                }, i);
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.6,-1.6)));
                ModUtils.setEntityPosition(missile_4, pos);
            }, i);
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.3,2.6,1.6)));
                ModUtils.setEntityPosition(missile_5, pos);
                }, i);
        }

        //Shoot Projectiles
        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) MobConfig.magic_missile_velocity;

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
                    });
        }, 39);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0.8)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) MobConfig.magic_missile_velocity;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_2,0F, speed);
            });
        }, 39);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -0.8)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) MobConfig.magic_missile_velocity;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_3,0F, speed);
            });
        }, 39);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -1.6)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) MobConfig.magic_missile_velocity;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_4,0F, speed);
            });
        }, 39);

        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 1.6)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) MobConfig.magic_missile_velocity;
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,missile_5,0F, speed);
            });
        }, 39);

    }
}
