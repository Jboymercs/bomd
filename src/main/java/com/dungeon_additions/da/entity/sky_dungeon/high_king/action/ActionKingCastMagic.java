package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.ProjectileLightRing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionKingCastMagic implements IActionDrake{
    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {

        actor.playSound(SoundsHandler.GARGOYLE_CAST_BASIC, 2.5f, 0.8f / (actor.getRNG().nextFloat() * 0.4F + 0.4f));


        actor.addEvent(() -> {
            ProjectileLightRing lightRing = new ProjectileLightRing(actor.world, actor, actor.getAttack(), target);
            Vec3d setPos = actor.neckPart1.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 4)));
            lightRing.setPosition(setPos.x, setPos.y, setPos.z);
            lightRing.setTravelRange(70F);

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = setPos.subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Z_AXIS), fromTargetTooActor, 0).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 0.8;
            actor.world.spawnEntity(lightRing);
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos, lightRing,0F, speed);
            });
        }, 1);

        actor.addEvent(() -> {
            ProjectileLightRing lightRing = new ProjectileLightRing(actor.world, actor, actor.getAttack(), target);
            Vec3d setPos = actor.neckPart1.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 4)));
            lightRing.setPosition(setPos.x, setPos.y, setPos.z);
            lightRing.setTravelRange(70F);

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = setPos.subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Z_AXIS), fromTargetTooActor, 30).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 0.8;
            actor.world.spawnEntity(lightRing);
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos, lightRing,0F, speed);
            });
        }, 10);

        actor.addEvent(() -> {
            ProjectileLightRing lightRing = new ProjectileLightRing(actor.world, actor, actor.getAttack(), target);
            Vec3d setPos = actor.neckPart1.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 4)));
            lightRing.setPosition(setPos.x, setPos.y, setPos.z);
            lightRing.setTravelRange(70F);

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = setPos.subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Z_AXIS), fromTargetTooActor, -30).normalize().scale(1);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);

            float speed = (float) 0.8;
            actor.world.spawnEntity(lightRing);
            ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                ModUtils.throwProjectileNoSpawn(pos, lightRing,0F, speed);
            });
        }, 20);
    }
}
