package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileKingBlood;
import com.dungeon_additions.da.entity.void_dungeon.ProjectileVoidClysmBolt;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionVoidFlay implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float pitch = 0; // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 1.3f;
        float damage = ModConfig.void_hammer_projectile_damage;
        Vec3d playerLookVec = actor.getLookVec();
        ProjectileVoidClysmBolt blood = new ProjectileVoidClysmBolt(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0));
        Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + playerLookVec.y + actor.getEyeHeight(), actor. posZ + playerLookVec.z * 1.4D);

        blood.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw - 15, 0.0F, speed, inaccuracy);
        blood.rotationYaw = actor.rotationYaw - 15;
        blood.rotationPitch = actor.rotationPitch;
        blood.setTravelRange(24f);
        actor.world.spawnEntity(blood);

        ProjectileVoidClysmBolt blood2 = new ProjectileVoidClysmBolt(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0));

        blood2.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood2.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        blood2.rotationYaw = actor.rotationYaw;
        blood2.rotationPitch = actor.rotationPitch;
        blood2.setTravelRange(24f);
        actor.world.spawnEntity(blood2);

        ProjectileVoidClysmBolt blood3 = new ProjectileVoidClysmBolt(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0));

        blood3.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood3.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw + 15, 0.0F, speed, inaccuracy);
        blood3.rotationYaw = actor.rotationYaw + 15;
        blood3.rotationPitch = actor.rotationPitch;
        blood3.setTravelRange(24f);
        actor.world.spawnEntity(blood3);
    }
}
