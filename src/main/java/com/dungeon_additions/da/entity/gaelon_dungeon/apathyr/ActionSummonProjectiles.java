package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.ProjectileGhost;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionSummonProjectiles implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 3, 0)));

        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0)));

        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-30, 30)).normalize().scale(4.5);
        Vec3d lineStart = targetPos.subtract(lineDir);
        Vec3d lineEnd = targetPos.add(lineDir);
        ModUtils.lineCallback(lineStart, lineEnd, 5, (pos, i) -> {
            ProjectileGhost missile = new ProjectileGhost(actor.world, actor, (float) (actor.getAttack() * 0.8));
            missile.setPosition(relPos.x, relPos.y, relPos.z);
            missile.setTravelRange(40);
            float speed = (float) 0.65;
            missile.rotationPitch = 0;
            missile.rotationYaw = actor.rotationYaw;
            actor.world.spawnEntity(missile);
            ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
        });
    }
}
