package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyWave;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionScatteredSmallHolyWave implements IActionKing{

    private int distance;

    public ActionScatteredSmallHolyWave(int distanceFrom) {
        this.distance = distanceFrom;
    }
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.6f;
        if(distance > 18) {
            speed = 1.2F;
        }
        float pitch = 0; // Projectiles aim straight ahead always

        EntityKingHolyWave wave = new EntityKingHolyWave(actor.world, actor, actor.getAttack());
        EntityKingHolyWave wave2 = new EntityKingHolyWave(actor.world, actor, actor.getAttack());
        EntityKingHolyWave wave3 = new EntityKingHolyWave(actor.world, actor, actor.getAttack());
        EntityKingHolyWave wave4 = new EntityKingHolyWave(actor.world, actor, actor.getAttack());
        EntityKingHolyWave wave5 = new EntityKingHolyWave(actor.world, actor, actor.getAttack());

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));

        wave.setPosition(relPos.x, relPos.y, relPos.z);
        wave2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        wave3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        wave4.setPosition(relPos4.x, relPos4.y, relPos4.z);
        wave5.setPosition(relPos5.x, relPos5.y, relPos5.z);
        wave.shoot(actor, pitch, actor.rotationYaw - 55, 0.0F, speed, inaccuracy);
        wave2.shoot(actor, pitch, actor.rotationYaw - 30, 0.0F, speed, inaccuracy);
        wave3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        wave4.shoot(actor, pitch, actor.rotationYaw + 30, 0.0F, speed, inaccuracy);
        wave5.shoot(actor, pitch, actor.rotationYaw + 55, 0.0F, speed, inaccuracy);
        wave.rotationYaw = actor.rotationYaw -55;
        wave2.rotationYaw = actor.rotationYaw -30;
        wave3.rotationYaw = actor.rotationYaw;
        wave4.rotationYaw = actor.rotationYaw + 30;
        wave5.rotationYaw = actor.rotationYaw + 55;
        wave.setTravelRange(40f);
        wave2.setTravelRange(40F);
        wave3.setTravelRange(40F);
        wave4.setTravelRange(40F);
        wave5.setTravelRange(40F);
        actor.world.spawnEntity(wave);
        actor.world.spawnEntity(wave2);
        actor.world.spawnEntity(wave3);
        actor.world.spawnEntity(wave4);
        actor.world.spawnEntity(wave5);
    }
}
