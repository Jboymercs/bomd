package com.dungeon_additions.da.entity.void_dungeon.voidclysm_action;

import com.dungeon_additions.da.entity.ai.IActionVoidclysm;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidiclysm;
import com.dungeon_additions.da.entity.void_dungeon.ProjectileVoidClysmBolt;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionCastVoidBolts implements IActionVoidclysm {

    public ActionCastVoidBolts() {

    }


    @Override
    public void performAction(EntityVoidiclysm actor, EntityLivingBase target) {
        double health = actor.getHealth() / actor.getMaxHealth();

        if(health < 0.5) {
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-45, 45)).normalize().scale(8);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);
            ModUtils.lineCallback(lineStart, lineEnd, 9, (pos, i) -> {
                ProjectileVoidClysmBolt missile = new ProjectileVoidClysmBolt(actor.world, actor, (float) (actor.getAttack() * 0.8));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.setTravelRange(40);
                float speed = (float) 1.2;
                missile.rotationPitch = actor.getPitch();
                missile.rotationYaw = actor.rotationYaw;
                actor.world.spawnEntity(missile);
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
            });
        } else {
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-45, 45)).normalize().scale(4);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);
            ModUtils.lineCallback(lineStart, lineEnd, 5, (pos, i) -> {
                ProjectileVoidClysmBolt missile = new ProjectileVoidClysmBolt(actor.world, actor, (float) (actor.getAttack() * 0.8));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.setTravelRange(40);
                float speed = (float) 1.2;
                missile.rotationPitch = actor.getPitch();
                missile.rotationYaw = actor.rotationYaw;
                actor.world.spawnEntity(missile);
                ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
            });
        }
    }
}
