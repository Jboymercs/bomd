package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.rot_knights.ProjectileDelayedPoisonCloud;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionRotCloud implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.25f;
        float pitch = 0;
        Random rand = new Random();
        actor.addEvent(()-> {
            ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 22);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
            cloud.setPosition(relPos.x, relPos.y, relPos.z);
            cloud.shoot(actor, pitch, actor.rotationYaw + 45, 0.0F, speed, inaccuracy);
            actor.world.spawnEntity(cloud);
            actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 1);

        actor.addEvent(()-> {
            ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 22);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
            cloud.setPosition(relPos.x, relPos.y, relPos.z);
            cloud.shoot(actor, pitch, actor.rotationYaw + 23, 0.0F, speed, inaccuracy);
            actor.world.spawnEntity(cloud);
            actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 5);

        actor.addEvent(()-> {
            ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 22);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
            cloud.setPosition(relPos.x, relPos.y, relPos.z);
            cloud.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            actor.world.spawnEntity(cloud);
            actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 10);

        actor.addEvent(()-> {
            ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 22);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
            cloud.setPosition(relPos.x, relPos.y, relPos.z);
            cloud.shoot(actor, pitch, actor.rotationYaw - 23, 0.0F, speed, inaccuracy);
            actor.world.spawnEntity(cloud);
            actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 25);

        actor.addEvent(()-> {
            ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 22);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
            cloud.setPosition(relPos.x, relPos.y, relPos.z);
            cloud.shoot(actor, pitch, actor.rotationYaw - 45, 0.0F, speed, inaccuracy);
            actor.world.spawnEntity(cloud);
            actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 20);
    }
}
