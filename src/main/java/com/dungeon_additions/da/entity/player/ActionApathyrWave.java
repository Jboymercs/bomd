package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.apathyr.ProjectileCrystalWave;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ActionApathyrWave implements IActionPlayer {

    private float damage;

    public ActionApathyrWave(float damage) {
        this.damage = damage;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        float inaccuracy = 0.0f;
        float speed = 0.35f;
        ProjectileCrystalWave projectile = new ProjectileCrystalWave(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), null);
        ProjectileCrystalWave projectile2 = new ProjectileCrystalWave(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), null);
        ProjectileCrystalWave projectile3 = new ProjectileCrystalWave(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), null);
        ProjectileCrystalWave projectile4 = new ProjectileCrystalWave(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), null);
        ProjectileCrystalWave projectile5 = new ProjectileCrystalWave(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), null);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 0, 0)));

        projectile.setPosition(relPos.x, relPos.y, relPos.z);
        projectile2.setPosition(relPos.x, relPos.y, relPos.z);
        projectile3.setPosition(relPos.x, relPos.y, relPos.z);
        projectile4.setPosition(relPos.x, relPos.y, relPos.z);
        projectile5.setPosition(relPos.x, relPos.y, relPos.z);

        projectile.shoot(actor, 0, actor.rotationYaw, 0.0F, speed, inaccuracy);
        projectile2.shoot(actor, 0, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
        projectile3.shoot(actor, 0, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);
        projectile4.shoot(actor, 0, actor.rotationYaw - 60, 0.0F, speed, inaccuracy);
        projectile5.shoot(actor, 0, actor.rotationYaw + 60, 0.0F, speed, inaccuracy);
        projectile.rotationYaw = actor.rotationYaw;
        projectile2.rotationYaw = actor.rotationYaw - 30;
        projectile3.rotationYaw = actor.rotationYaw + 30;
        projectile4.rotationYaw = actor.rotationYaw - 60;
        projectile5.rotationYaw = actor.rotationYaw + 60;

        projectile.setTravelRange(16F);
        projectile2.setTravelRange(16F);
        projectile3.setTravelRange(16F);
        projectile4.setTravelRange(16F);
        projectile5.setTravelRange(16F);

        actor.world.spawnEntity(projectile);
        actor.world.spawnEntity(projectile2);
        actor.world.spawnEntity(projectile3);
        actor.world.spawnEntity(projectile4);
        actor.world.spawnEntity(projectile5);
    }
}
