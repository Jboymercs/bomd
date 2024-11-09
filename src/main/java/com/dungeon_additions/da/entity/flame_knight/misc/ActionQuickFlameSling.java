package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;


public class ActionQuickFlameSling implements IAction {

    public final Supplier<Projectile> supplier;

    public ActionQuickFlameSling(Supplier<Projectile> p) {
        supplier = p;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.55f;
        float pitch = 0; // Projectiles aim straight ahead always
        actor.playSound(SoundsHandler.B_KNIGHT_FLAME_SLING_FAST, 1.0f, 0.7f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));
        Projectile projectile = supplier.get();
        Projectile projectile1 = supplier.get();
        Projectile projectile2 = supplier.get();
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, -1)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 1)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile1.setPosition(relPos2.x, relPos2.y, relPos2.z);
        projectile2.setPosition(relPos3.x, relPos3.y, relPos3.z);
        projectile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile1.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile.setTravelRange(40f);
        projectile1.setTravelRange(40F);
        projectile2.setTravelRange(40F);
        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
    }
}
