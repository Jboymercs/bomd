package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionLaunchSIdeProjectiles implements IAction {

    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionLaunchSIdeProjectiles(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.75f;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 1.0f, 0.4F / (new Random().nextFloat() * 0.4F + 0.4f));
        Projectile projectile = projectileSupplier.get();
        Projectile projectile1 = projectileSupplier.get();
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-3, 0, 0)));
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos.x, relPos.y, relPos.z);


        projectile.shoot(actor, pitch, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
        projectile1.shoot(actor, pitch, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);


        projectile.setTravelRange(12f);
        projectile1.setTravelRange(12F);


        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);

    }
}
