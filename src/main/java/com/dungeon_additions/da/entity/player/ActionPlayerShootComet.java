package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicFireBall;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionPlayerShootComet implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {

        ProjectileMagicFireBall fireBall = new ProjectileMagicFireBall(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * 0.68));
        fireBall.setTravelRange(40);
        fireBall.setNoGravity(true);
        float speed = (float) (MobConfig.magic_fireball_velocity - 0.6);
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,3.2,0)));
        fireBall.setPosition(relPos.x, relPos.y, relPos.z);
        fireBall.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
        actor.world.spawnEntity(fireBall);
    }
}
