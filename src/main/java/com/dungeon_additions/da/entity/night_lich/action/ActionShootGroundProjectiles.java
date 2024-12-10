package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.function.Supplier;

public class ActionShootGroundProjectiles implements IActionLich{

    public final Supplier<Projectile> supplier;

    public ActionShootGroundProjectiles(Supplier<Projectile> p) {
        supplier = p;
    }
    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.55f;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        Projectile projectile = supplier.get();
        Projectile projectile1 = supplier.get();
        Projectile projectile2 = supplier.get();
        Projectile projectile3 = supplier.get();
        Projectile projectile4 = supplier.get();
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 2)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 1)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, -1)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, -2)));
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos2.x, relPos2.y, relPos2.z);
        projectile2.setPosition(relPos3.x, relPos3.y, relPos3.z);
        projectile3.setPosition(relPos4.x, relPos4.y, relPos4.z);
        projectile4.setPosition(relPos5.x, relPos5.y, relPos5.z);
        projectile1.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile.setTravelRange(40f);
        projectile1.setTravelRange(40F);
        projectile2.setTravelRange(40F);
        projectile3.setTravelRange(40F);
        projectile4.setTravelRange(40F);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile);
    }
}
