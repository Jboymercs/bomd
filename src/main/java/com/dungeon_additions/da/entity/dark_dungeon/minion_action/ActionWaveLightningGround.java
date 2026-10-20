package com.dungeon_additions.da.entity.dark_dungeon.minion_action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionWaveLightningGround implements IAction {
    Supplier<Projectile> projectileSupplier;
    private float velocity;
    private float distance;
    private Vec3d relPos;
    private boolean isScattered;

    public ActionWaveLightningGround(Supplier<Projectile> projectileSupplier, float velocity, float distance, Vec3d relPos, boolean isScattered) {
        this.projectileSupplier = projectileSupplier;
        this.velocity = velocity;
        this.distance = distance;
        this.isScattered = isScattered;
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
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);

        if(isScattered) {
            projectile.shoot(actor, pitch, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
            projectile1.shoot(actor, pitch, actor.rotationYaw + 45, 0.0F, speed, inaccuracy);
            projectile2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile3.shoot(actor, pitch, actor.rotationYaw - 45, 0.0F, speed, inaccuracy);
            projectile4.shoot(actor, pitch, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
        } else {
            projectile.shoot(actor, pitch, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);
            projectile1.shoot(actor, pitch, actor.rotationYaw + 15, 0.0F, speed, inaccuracy);
            projectile2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile3.shoot(actor, pitch, actor.rotationYaw - 15, 0.0F, speed, inaccuracy);
            projectile4.shoot(actor, pitch, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
        }

        projectile.setTravelRange(distance);
        projectile1.setTravelRange(distance);
        projectile2.setTravelRange(distance);
        projectile3.setTravelRange(distance);
        projectile4.setTravelRange(distance);
        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
    }
}
