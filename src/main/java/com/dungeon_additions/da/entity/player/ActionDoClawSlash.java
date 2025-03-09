package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyWave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class ActionDoClawSlash implements IActionPlayer{

    private boolean hasOffhand;

    public ActionDoClawSlash(boolean hasOffhand) {
        this.hasOffhand = hasOffhand;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        float damage = hasOffhand ? 13 : 8;
        Vec3d playerLookVec = actor.getLookVec();
        Vec3d relPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY, actor. posZ + playerLookVec.z * 1.4D);
        float inaccuracy = 0.0f;
        float speed = 0.4f;

        EntityKingHolyWave wave = new EntityKingHolyWave(actor.world, actor, damage);
        EntityKingHolyWave wave2 = new EntityKingHolyWave(actor.world, actor, damage);
        EntityKingHolyWave wave3 = new EntityKingHolyWave(actor.world, actor, damage);
        EntityKingHolyWave wave4 = new EntityKingHolyWave(actor.world, actor, damage);
        EntityKingHolyWave wave5 = new EntityKingHolyWave(actor.world, actor, damage);

        wave.setPosition(relPos.x, relPos.y, relPos.z);
        wave2.setPosition(relPos.x, relPos.y, relPos.z);
        wave3.setPosition(relPos.x, relPos.y, relPos.z);
        wave4.setPosition(relPos.x, relPos.y, relPos.z);
        wave5.setPosition(relPos.x, relPos.y, relPos.z);


        wave.shoot(actor, 0, actor.rotationYaw - 40, 0.0F, speed, inaccuracy);
        wave2.shoot(actor, 0, actor.rotationYaw - 20, 0.0F, speed, inaccuracy);
        wave3.shoot(actor,0, actor.rotationYaw, 0.0F, speed, inaccuracy);
        wave4.shoot(actor, 0, actor.rotationYaw + 20, 0.0F, speed, inaccuracy);
        wave5.shoot(actor, 0, actor.rotationYaw + 40, 0.0F, speed, inaccuracy);
        wave.rotationYaw = actor.rotationYaw -40;
        wave2.rotationYaw = actor.rotationYaw -20;
        wave3.rotationYaw = actor.rotationYaw;
        wave4.rotationYaw = actor.rotationYaw + 20;
        wave5.rotationYaw = actor.rotationYaw + 40;
        wave.setTravelRange(10f);
        wave2.setTravelRange(12F);
        wave3.setTravelRange(16F);
        wave4.setTravelRange(12F);
        wave5.setTravelRange(10F);

        actor.world.spawnEntity(wave);
        actor.world.spawnEntity(wave2);
        actor.world.spawnEntity(wave3);
        actor.world.spawnEntity(wave4);
        actor.world.spawnEntity(wave5);
    }
}
