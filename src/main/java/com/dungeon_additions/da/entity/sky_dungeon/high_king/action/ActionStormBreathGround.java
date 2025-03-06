package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileStormWind;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionStormBreathGround implements IActionDrake{
    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        for(int i = 0; i <= 80; i+= 3) {
            actor.addEvent(()-> {
                ProjectileStormWind wind = new ProjectileStormWind(actor.world, actor,(float) (actor.getAttack() * 0.6));
                Vec3d setPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(6, 1.8, 0)));
                wind.setPosition(setPos.x, setPos.y, setPos.z);
                wind.setTravelRange(50F);

                if(actor.targetBreathPosition != null) {
                    Vec3d relShootPos = actor.targetBreathPosition.add(ModRand.range(-2, 2), 1.4 + ModRand.range(-2, 2), ModRand.range(-2, 2));
                    Vec3d fromTargetTooActor = setPos.subtract(relShootPos);
                    Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                    Vec3d lineStart = relShootPos.subtract(lineDir);
                    Vec3d lineEnd = relShootPos.add(lineDir);

                    actor.world.spawnEntity(wind);
                    float speed = (float) 0.9;

                    actor.world.spawnEntity(wind);
                    ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                        ModUtils.throwProjectileNoSpawn(pos, wind,0F, speed);
                    });
                }
            }, i);
        }
    }
}
