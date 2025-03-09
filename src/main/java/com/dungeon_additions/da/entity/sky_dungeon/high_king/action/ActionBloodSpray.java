package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action.IActionKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileKingBlood;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionBloodSpray implements IActionKing {
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {

        float pitch = 15; // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 1.0f;

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.25), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.7, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 1);

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.25), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.7, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 2);

        actor.addEvent(()-> {
            ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, (float) (actor.getAttack() * 0.25), false);
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2, 1.7, 0)));
            blood.setPosition(relPos.x, relPos.y, relPos.z);
            blood.shoot(actor, pitch, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);
            blood.setTravelRange(40f);
            actor.world.spawnEntity(blood);
        }, 3);
    }
}
