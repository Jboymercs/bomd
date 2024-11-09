package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionFlameSling implements IAction {
    public final Supplier<Projectile> supplier;

    private boolean isLarge;
    public ActionFlameSling(Supplier<Projectile> p, boolean isLarge) {
        supplier = p;
        this.isLarge = isLarge;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        //spawns 5 projectiles relative to the actor
        actor.playSound(SoundsHandler.B_KNIGHT_FLAME_SLING, 1.0f, 0.7f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));
        float inaccuracy = 0.0f;
        float speed = 0.55f;
        float pitch = 0; // Projectiles aim straight ahead always
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
        if(this.isLarge) {
            Projectile projectile5 = supplier.get();
            Projectile projectile6 = supplier.get();
            Projectile projectile7 = supplier.get();
            Projectile projectile8 = supplier.get();
            Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 3)));
            Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, -3)));
            Vec3d relPos8 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 4)));
            Vec3d relPos9 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, -4)));
            projectile5.setPosition(relPos6.x, relPos6.y, relPos6.z);
            projectile6.setPosition(relPos7.x, relPos7.y, relPos7.z);
            projectile7.setPosition(relPos8.x, relPos8.y, relPos8.z);
            projectile8.setPosition(relPos9.x, relPos9.y, relPos9.z);
            projectile5.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile6.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile7.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile8.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            projectile5.setTravelRange(40F);
            projectile6.setTravelRange(40F);
            projectile7.setTravelRange(40F);
            projectile8.setTravelRange(40F);
            actor.world.spawnEntity(projectile5);
            actor.world.spawnEntity(projectile6);
            actor.world.spawnEntity(projectile7);
            actor.world.spawnEntity(projectile8);
        }
    }
}
