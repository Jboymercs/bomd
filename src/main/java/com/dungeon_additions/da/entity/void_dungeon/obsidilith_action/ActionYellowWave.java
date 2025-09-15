package com.dungeon_additions.da.entity.void_dungeon.obsidilith_action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.miniboss.ProjectileYellowWave;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class ActionYellowWave implements IAction {

    Supplier<Projectile> projectileSupplier;


    public ActionYellowWave(Supplier<Projectile> projectileSupplier) {
        this.projectileSupplier = projectileSupplier;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
            doEcheleonWave(actor, 0);
            actor.addEvent(() -> doEcheleonWave(actor, 15), 10);
            actor.addEvent(() -> doEcheleonWave(actor, 30), 20);
            actor.addEvent(() -> doEcheleonWave(actor, 45), 30);
            actor.addEvent(() -> doEcheleonWave(actor, 60), 40);
            actor.addEvent(() -> doEcheleonWave(actor, 75), 50);
            actor.addEvent(() -> doEcheleonWave(actor, 90), 60);

    }

    private void doEcheleonWave(EntityLivingBase actor, float additive) {
        float inaccuracy = 0.0f;
        float speed = 0.4f;
        actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 1.0f, 1.0f);
        Projectile projectile = projectileSupplier.get();
        Projectile projectile2 = projectileSupplier.get();
        Projectile projectile3 = projectileSupplier.get();
        Projectile projectile4 = projectileSupplier.get();
        Projectile projectile5 = projectileSupplier.get();
        Projectile projectile6 = projectileSupplier.get();
        Projectile projectile7 = projectileSupplier.get();
        Projectile projectile8 = projectileSupplier.get();

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0.3, 0)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0.3, 2)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.3, 2)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 0.3, 2)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 0.3, 0)));
        Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0.3, -2)));
        Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.3, -2)));
        Vec3d relPos8 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 0.3, -2)));


        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos3.x, relPos3.y, relPos3.z);
        projectile3.setPosition(relPos4.x, relPos4.y, relPos4.z);
        projectile4.setPosition(relPos5.x, relPos5.y, relPos5.z);
        projectile5.setPosition(relPos2.x, relPos2.y, relPos2.z);
        projectile6.setPosition(relPos6.x, relPos6.y, relPos6.z);
        projectile7.setPosition(relPos7.x, relPos7.y, relPos7.z);
        projectile8.setPosition(relPos8.x, relPos8.y, relPos8.z);

        projectile.shoot(actor, 0, actor.rotationYaw + additive, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, 0, actor.rotationYaw - (45 - additive), 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, 0, actor.rotationYaw - (90 - additive), 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, 0, actor.rotationYaw - (135 - additive), 0.0F, speed, inaccuracy);
        projectile5.shoot(actor, 0, actor.rotationYaw - (180 - additive), 0.0F, speed, inaccuracy);
        projectile6.shoot(actor, 0, actor.rotationYaw + (45 + additive), 0.0F, speed, inaccuracy);
        projectile7.shoot(actor, 0, actor.rotationYaw + (90  + additive), 0.0F, speed, inaccuracy);
        projectile8.shoot(actor, 0, actor.rotationYaw + (135  + additive), 0.0F, speed, inaccuracy);
        projectile.rotationYaw = actor.rotationYaw + additive;
        projectile2.rotationYaw = actor.rotationYaw - (45 - additive);
        projectile3.rotationYaw = actor.rotationYaw - (90 - additive);
        projectile4.rotationYaw = actor.rotationYaw - (135  - additive);
        projectile5.rotationYaw = actor.rotationYaw - (180  - additive);
        projectile6.rotationYaw = actor.rotationYaw + (45 + additive);
        projectile7.rotationYaw = actor.rotationYaw + (90  + additive);
        projectile8.rotationYaw = actor.rotationYaw + (135  + additive);

        projectile.setTravelRange(20F);
        projectile2.setTravelRange(20F);
        projectile3.setTravelRange(20F);
        projectile4.setTravelRange(20F);
        projectile5.setTravelRange(20F);
        projectile6.setTravelRange(20F);
        projectile7.setTravelRange(20F);
        projectile8.setTravelRange(20F);

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
