package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.entity.rot_knights.ProjectileDelayedPoisonCloud;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionCastPoisonMist implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        shootPoisonCloud(actor, 0);
        shootPoisonCloud(actor, 30);
        shootPoisonCloud(actor, -30);
    }

    private void shootPoisonCloud(EntityPlayer actor, float offset) {
        float inaccuracy = 0.0f;
        float speed = 0.2f;
        float pitch = 0;
        float damage = PotionTrinketConfig.pocket_poison_damage + ModUtils.addMageSetBonus(actor, 0);
        Vec3d lookVec = actor.getLookVec();
        ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) damage, 25);
        Vec3d relPos = new Vec3d(actor.posX + lookVec.x * 1.4D, actor.posY + 1.4, actor.posZ + lookVec.z * 1.4D);
        cloud.setPosition(relPos.x, relPos.y, relPos.z);
        cloud.shoot(actor, pitch, actor.rotationYaw + offset, 0.0F, speed, inaccuracy);
        actor.world.spawnEntity(cloud);
    }
}
