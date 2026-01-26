package com.dungeon_additions.da.entity.desert_dungeon.boss.warlord;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertOrb;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileKingBlood;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionWarlordCutsProjectileSplay implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float pitch = 20; // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 0.8f;

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.5, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 2);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, -2)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 4);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, 2)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 6);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 1.5, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 180, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 8);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.5, -1)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 45, 0.0F, speed * 1.5F, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 10);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 1.5, -1)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 135, 0.0F, speed * 1.5F, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 12);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.5, 1)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw - 45, 0.0F, speed * 1.5F, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 14);

        actor.addEvent(()-> {
            ProjectileDesertOrb blood = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack()), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 1.5, 1)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw - 135, 0.0F, speed * 1.5F, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 16);
    }
}
