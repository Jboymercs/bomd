package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.ai.IActionProjectile;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileKingBlood;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionBloodBomb implements IActionKing {
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        float pitch = 15; // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 0.8f;
        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.5), true);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(4, 0, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 20, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 2);

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.5), true);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -1.25, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, 0, actor.rotationYaw, 0.0F, 0, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 4);


        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.5), true);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -2)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 110, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 6);

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.5), true);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(-2, 0, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 200, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 8);

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.5), true);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 2)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw - 70, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 11);
    }
}
