package com.dungeon_additions.da.entity.dark_dungeon.minion_action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.ProjectileSorceryDagger;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionCastRedProjectiles implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ProjectileSorceryDagger dagger = new ProjectileSorceryDagger(actor.world, actor,actor.getAttack() * 0.8F);
        ProjectileSorceryDagger dagger2 = new ProjectileSorceryDagger(actor.world, actor,actor.getAttack() * 0.8F);
        ProjectileSorceryDagger dagger3 = new ProjectileSorceryDagger(actor.world, actor,actor.getAttack() * 0.8F);
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3.25,0)));
        dagger.setPosition(relPos.x, relPos.y, relPos.z);
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3,0.75)));
        dagger2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3,-0.75)));
        dagger3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        actor.world.spawnEntity(dagger);
        actor.world.spawnEntity(dagger2);
        actor.world.spawnEntity(dagger3);
        dagger.setTravelRange(120F);
        dagger2.setTravelRange(120F);
        dagger3.setTravelRange(120F);
        int randomTimer = 20 + ModRand.range(10, 30);
        int randomTimer2 = 30 + ModRand.range(20, 50);
        int randomTimer3 = 40 + ModRand.range(30, 60);

        for(int i = 0; i < randomTimer; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3.25,0)));
                dagger.rotationYaw = actor.rotationYaw;
                ModUtils.setEntityPosition(dagger, pos);
            }, i);
        }

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.5f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.1;
            Vec3d targetPos = target.getPositionEyes(1.0F).add(0, -0.25, 0);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,dagger,0F, speed);
                dagger.rotationYaw = actor.rotationYaw;
            });
        }, randomTimer);

        for(int i = 0; i < randomTimer2; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3,0.75)));
                dagger2.rotationYaw = actor.rotationYaw;
                ModUtils.setEntityPosition(dagger2, pos);
            }, i);
        }

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.5f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.1;
            Vec3d targetPos = target.getPositionEyes(1.0F).add(0, -0.25, 0);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,dagger2,0F, speed);
                dagger2.rotationYaw = actor.rotationYaw;
            });
        }, randomTimer2);

        for(int i = 0; i < randomTimer3; i++) {
            actor.addEvent(()-> {
                Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.1,3,-0.75)));
                dagger3.rotationYaw = actor.rotationYaw;
                ModUtils.setEntityPosition(dagger3, pos);
            }, i);
        }

        actor.addEvent(()-> {
            actor.playSound(SoundsHandler.ROH_THROW_DAGGERS, 1.5f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            float speed = (float) 1.1;
            Vec3d targetPos = target.getPositionEyes(1.0F).add(0, -0.25, 0);

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos,dagger3,0F, speed);
                dagger3.rotationYaw = actor.rotationYaw;
            });
        }, randomTimer3);
    }
}
