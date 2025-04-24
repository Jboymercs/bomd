package com.dungeon_additions.da.entity.flame_knight.volatile_action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionSecondFlameWave implements IAction {

    public final Supplier<Projectile> supplier;

    public ActionSecondFlameWave(Supplier<Projectile> p) {
        supplier = p;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        actor.playSound(SoundsHandler.B_KNIGHT_FLAME_SLING, 1.0f, 0.7f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));
        float inaccuracy = 0.0f;
        float speed = 0.6f;
        float pitch = 0; // Projectiles aim straight ahead always
        Projectile projectile = supplier.get();
        Projectile projectile1 = supplier.get();
        Projectile projectile2 = supplier.get();
        Projectile projectile3 = supplier.get();
        Projectile projectile4 = supplier.get();
        Projectile projectile5 = supplier.get();
        Projectile projectile6 = supplier.get();

        Vec3d relPosOri = actor.getPositionVector();

        //front phalanx
        projectile.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);
        projectile1.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);
        projectile2.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);

        //angled Phalanx Forward Left and Right
        projectile3.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);
        projectile4.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);

        //Side Phalanx
        projectile5.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);
        projectile6.setPosition(relPosOri.x, relPosOri.y, relPosOri.z);

        projectile1.shoot(actor, pitch, actor.rotationYaw + 22, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, pitch, actor.rotationYaw - 22, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, pitch, actor.rotationYaw + 67, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, pitch, actor.rotationYaw - 67, 0.0F, speed, inaccuracy);
        projectile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);

        projectile5.shoot(actor, pitch, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
        projectile6.shoot(actor, pitch, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);

        projectile.setTravelRange(40f);
        projectile1.setTravelRange(40F);
        projectile2.setTravelRange(40F);
        projectile3.setTravelRange(40F);
        projectile4.setTravelRange(40F);
        projectile5.setTravelRange(40F);
        projectile6.setTravelRange(40F);

        actor.world.spawnEntity(projectile1);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile5);
        actor.world.spawnEntity(projectile6);
    }
}
