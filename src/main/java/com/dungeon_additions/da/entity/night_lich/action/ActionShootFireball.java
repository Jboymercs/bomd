package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicFireBall;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionShootFireball implements IActionLich{


    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        Vec3d targetedPos = target.getPositionVector();
        ProjectileMagicFireBall fireBall = new ProjectileMagicFireBall(actor.world, actor, actor.getAttack());
        fireBall.setTravelRange(50);
        fireBall.setNoGravity(true);
        ModUtils.throwProjectile(actor, targetedPos, fireBall, 0, (float) MobConfig.magic_fireball_velocity);
        actor.playSound(SoundsHandler.LICH_PREPARE_FIREBALL, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
    }
}
