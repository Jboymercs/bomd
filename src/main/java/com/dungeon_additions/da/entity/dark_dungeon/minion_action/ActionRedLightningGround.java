package com.dungeon_additions.da.entity.dark_dungeon.minion_action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionRedLightningGround implements IAction {
    Supplier<Projectile> projectileSupplier;
    private float velocity;
    private float distance;
    private float projectileAngle;
    private Vec3d relPos;

    public ActionRedLightningGround(Supplier<Projectile> projectileSupplier, float velocity, float distance, float projectileAngle, Vec3d relPos) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
        this.distance = distance;
        this.projectileAngle = projectileAngle;
        this.relPos = relPos;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = velocity;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        Projectile projectile = projectileSupplier.get();
        Projectile projectile1 = projectileSupplier.get();
        Projectile projectile2 = projectileSupplier.get();
        Projectile projectile3 = projectileSupplier.get();
        Projectile projectile4 = projectileSupplier.get();
        Projectile projectile5 = projectileSupplier.get();
        Projectile projectile6 = projectileSupplier.get();
        Projectile projectile7 = projectileSupplier.get();
        Projectile projectile8 = projectileSupplier.get();
        Projectile projectile9 = projectileSupplier.get();
        Projectile projectile10 = projectileSupplier.get();
        Projectile projectile11 = projectileSupplier.get();
        Projectile projectile12 = projectileSupplier.get();
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);
        projectile5.setPosition(relPos.x, relPos.y, relPos.z);
        projectile6.setPosition(relPos.x, relPos.y, relPos.z);
        projectile7.setPosition(relPos.x, relPos.y, relPos.z);
        projectile8.setPosition(relPos.x, relPos.y, relPos.z);
        projectile9.setPosition(relPos.x, relPos.y, relPos.z);
        projectile10.setPosition(relPos.x, relPos.y, relPos.z);
        projectile11.setPosition(relPos.x, relPos.y, relPos.z);
        projectile12.setPosition(relPos.x, relPos.y, relPos.z);
        projectile11.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 6), 0.0F, speed, inaccuracy);
        projectile.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 5), 0.0F, speed, inaccuracy);
        projectile1.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 4), 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 3), 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 2), 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, pitch, actor.rotationYaw - projectileAngle, 0.0F, speed, inaccuracy);
        // Center
        projectile5.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        //
        projectile6.shoot(actor, pitch, actor.rotationYaw + projectileAngle, 0.0F, speed, inaccuracy);
        projectile7.shoot(actor, pitch, actor.rotationYaw + (projectileAngle * 2), 0.0F, speed, inaccuracy);
        projectile8.shoot(actor, pitch, actor.rotationYaw + (projectileAngle * 3), 0.0F, speed, inaccuracy);
        projectile9.shoot(actor, pitch, actor.rotationYaw + (projectileAngle * 4), 0.0F, speed, inaccuracy);
        projectile10.shoot(actor, pitch, actor.rotationYaw + (projectileAngle * 5), 0.0F, speed, inaccuracy);
        projectile12.shoot(actor, pitch, actor.rotationYaw - (projectileAngle * 6), 0.0F, speed, inaccuracy);
        projectile.setTravelRange(distance);
        projectile1.setTravelRange(distance);
        projectile2.setTravelRange(distance);
        projectile3.setTravelRange(distance);
        projectile4.setTravelRange(distance);
        projectile5.setTravelRange(distance);
        projectile6.setTravelRange(distance);
        projectile7.setTravelRange(distance);
        projectile8.setTravelRange(distance);
        projectile9.setTravelRange(distance);
        projectile10.setTravelRange(distance);
        projectile11.setTravelRange(distance);
        projectile12.setTravelRange(distance);
        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile5);
        actor.world.spawnEntity(projectile6);
        actor.world.spawnEntity(projectile7);
        actor.world.spawnEntity(projectile8);
        actor.world.spawnEntity(projectile9);
        actor.world.spawnEntity(projectile10);
        actor.world.spawnEntity(projectile11);
        actor.world.spawnEntity(projectile12);
    }
}
