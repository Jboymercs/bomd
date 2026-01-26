package com.dungeon_additions.da.entity.desert_dungeon.boss.colossus;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionColossusHiltSlam implements IAction {

    Supplier<Projectile> projectileSupplier;

    public ActionColossusHiltSlam(Supplier<Projectile> projectileSupplier) {
        this.projectileSupplier = projectileSupplier;
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
        Projectile projectile9 = projectileSupplier.get();

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 0)));

        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);
        projectile5.setPosition(relPos.x, relPos.y, relPos.z);
        projectile6.setPosition(relPos.x, relPos.y, relPos.z);
        projectile7.setPosition(relPos.x, relPos.y, relPos.z);
        projectile8.setPosition(relPos.x, relPos.y, relPos.z);
        projectile9.setPosition(relPos.x, relPos.y, relPos.z);

        projectile.shoot(actor, 0, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, 0, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, 0, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, 0, actor.rotationYaw - 60, 0.0F, speed, inaccuracy);
        projectile5.shoot(actor, 0, actor.rotationYaw + 60, 0.0F, speed, inaccuracy);
        projectile6.shoot(actor, 0, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
        projectile7.shoot(actor, 0, actor.rotationYaw + 135, 0.0F, speed, inaccuracy);
        projectile8.shoot(actor, 0, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
        projectile9.shoot(actor, 0, actor.rotationYaw - 135, 0.0F, speed, inaccuracy);

        projectile.rotationYaw = actor.rotationYaw;
        projectile2.rotationYaw = actor.rotationYaw - 30;
        projectile3.rotationYaw = actor.rotationYaw + 30;
        projectile4.rotationYaw = actor.rotationYaw - 60;
        projectile5.rotationYaw = actor.rotationYaw + 60;
        projectile6.rotationYaw = actor.rotationYaw + 90;
        projectile7.rotationYaw = actor.rotationYaw + 135;
        projectile8.rotationYaw = actor.rotationYaw - 90;
        projectile9.rotationYaw = actor.rotationYaw - 135;

        projectile.setTravelRange(24F);
        projectile2.setTravelRange(24F);
        projectile3.setTravelRange(24F);
        projectile4.setTravelRange(24F);
        projectile5.setTravelRange(24F);
        projectile6.setTravelRange(24F);
        projectile7.setTravelRange(24F);
        projectile8.setTravelRange(24F);
        projectile9.setTravelRange(24F);

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile5);
        actor.world.spawnEntity(projectile6);
        actor.world.spawnEntity(projectile7);
        actor.world.spawnEntity(projectile8);
        actor.world.spawnEntity(projectile9);
    }
}
