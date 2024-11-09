package com.dungeon_additions.da.entity.blossom.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionLeafAttack implements IAction {
    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionLeafAttack(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionEyes(1);
        Vec3d fromTargetToActor = actor.getPositionVector().subtract(targetPos);
        actor.playSound(SoundsHandler.BLOSSOM_PETAL_WAVE, 2.0f, 1.0f);
        Vec3d lineDirection = ModUtils.rotateVector2(
                        fromTargetToActor.crossProduct(ModUtils.Y_AXIS),
                        fromTargetToActor,
                        ModRand.range(-45, 45))
                .normalize()
                .scale(4);

        Vec3d lineStart = targetPos.subtract(lineDirection);
        Vec3d lineEnd = targetPos.add(lineDirection);

        ModUtils.lineCallback(lineStart, lineEnd, 11, (pos, i) -> {
            Projectile projectile = projectileSupplier.get();
            projectile.setTravelRange(30);
            projectile.setNoGravity(true);
            ModUtils.throwProjectile(actor, pos, projectile, 0, velocity);
        });
    }
}
