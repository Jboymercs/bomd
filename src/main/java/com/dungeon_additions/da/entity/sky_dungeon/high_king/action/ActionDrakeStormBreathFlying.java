package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;


import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileStormBreath;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileStormWind;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionDrakeStormBreathFlying implements IActionDrake {

    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {

        //Spawns the Wind Projectiles
        for (int i = 0; i < 35; i += 2.5) {
            actor.addEvent(() -> {
                ProjectileStormWind wind_breath = new ProjectileStormWind(actor.world, actor, (float) (actor.getAttack() * 0.6));
                Vec3d setPos = actor.headPart.getPositionVector();

                Vec3d relPos1 = setPos.add(1, -1.3, 2.25);

                wind_breath.setTravelRange(60F);
                wind_breath.setPosition(relPos1.x, relPos1.y, relPos1.z);
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(ModRand.range(-3, 3), -0.5, ModRand.range(-3, 3))));

                Vec3d fromTargetTooActor = relPos1.subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                actor.world.spawnEntity(wind_breath);
                float speed = (float) 1.5;

                actor.world.spawnEntity(wind_breath);
                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                    ModUtils.throwProjectileNoSpawn(pos, wind_breath, 0F, speed);
                });

            }, i);
        }
        //Spawns the Tornadoes
        for (int t = 0; t < 35; t += 12) {
            actor.addEvent(() -> {
                ProjectileStormBreath breath1 = new ProjectileStormBreath(actor.world, actor, actor.getAttack(), target);
                Vec3d setPos = actor.headPart.getPositionVector();

                Vec3d relPos1 = setPos.add(1, -1.3, 2.25);
                breath1.setPosition(relPos1.x, relPos1.y, relPos1.z);
                breath1.setTravelRange(70F);
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -1.5, 0)));

                Vec3d fromTargetTooActor = relPos1.subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Z_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                float speed = (float) 1;
                actor.world.spawnEntity(breath1);
                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, breath1, 0F, speed);
                });
            }, t);
        }
    }



}
