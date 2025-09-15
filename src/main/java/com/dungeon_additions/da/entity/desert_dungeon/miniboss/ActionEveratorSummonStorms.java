package com.dungeon_additions.da.entity.desert_dungeon.miniboss;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionEveratorSummonStorms implements IAction {

    Supplier<Projectile> projectileSupplier;
    float velocity;

    public ActionEveratorSummonStorms(Supplier<Projectile> projectileSupplier, float velocity) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.6f;
        Projectile projectile = projectileSupplier.get();
        Projectile projectile2 = projectileSupplier.get();
        Projectile projectile3 = projectileSupplier.get();
        Projectile projectile4 = projectileSupplier.get();
        Projectile projectile5 = projectileSupplier.get();
        Projectile projectile6 = projectileSupplier.get();
        Projectile projectile7 = projectileSupplier.get();
        Projectile projectile8 = projectileSupplier.get();

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));

        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);
        projectile5.setPosition(relPos.x, relPos.y, relPos.z);
        projectile6.setPosition(relPos.x, relPos.y, relPos.z);
        projectile7.setPosition(relPos.x, relPos.y, relPos.z);
        projectile8.setPosition(relPos.x, relPos.y, relPos.z);

        projectile.shoot(actor, 0, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, 0, actor.rotationYaw - 45, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, 0, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, 0, actor.rotationYaw - 135, 0.0F, speed, inaccuracy);
        projectile5.shoot(actor, 0, actor.rotationYaw - 180, 0.0F, speed, inaccuracy);
        projectile6.shoot(actor, 0, actor.rotationYaw + 45, 0.0F, speed, inaccuracy);
        projectile7.shoot(actor, 0, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
        projectile8.shoot(actor, 0, actor.rotationYaw + 135, 0.0F, speed, inaccuracy);

        projectile.setTravelRange(16F);
        projectile2.setTravelRange(16F);
        projectile3.setTravelRange(16F);
        projectile4.setTravelRange(16F);
        projectile5.setTravelRange(16F);
        projectile6.setTravelRange(16F);
        projectile7.setTravelRange(16F);
        projectile8.setTravelRange(16F);

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile5);
        actor.world.spawnEntity(projectile6);
        actor.world.spawnEntity(projectile7);
        actor.world.spawnEntity(projectile8);
    }
}
