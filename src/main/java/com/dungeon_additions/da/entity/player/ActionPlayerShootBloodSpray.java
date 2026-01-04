package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.ProjectileKingBlood;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionPlayerShootBloodSpray implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float pitch = -10; // Projectiles aim straight ahead always
        float inaccuracy = 0.0f;
        float speed = 1.0f;
        boolean hasHelmet = actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.KINGS_HELMET;
        float damage = hasHelmet ? 17 : 10;
        Vec3d playerLookVec = actor.getLookVec();
        ProjectileKingBlood blood = new ProjectileKingBlood(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), false);
        Vec3d playerPos = new Vec3d(actor.posX + playerLookVec.x * 1.4D,actor.posY + playerLookVec.y + actor.getEyeHeight(), actor. posZ + playerLookVec.z * 1.4D);

        blood.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw - 15, 0.0F, speed, inaccuracy);
        blood.setTravelRange(40f);
        actor.world.spawnEntity(blood);

        ProjectileKingBlood blood2 = new ProjectileKingBlood(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), false);

        blood2.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood2.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        blood2.setTravelRange(40f);
        actor.world.spawnEntity(blood2);

        ProjectileKingBlood blood3 = new ProjectileKingBlood(actor.world, actor, damage + ModUtils.addMageSetBonus(actor, 0), false);

        blood3.setPosition(playerPos.x, playerPos.y, playerPos.z);
        blood3.shoot(actor, actor.rotationPitch + pitch, actor.rotationYaw + 15, 0.0F, speed, inaccuracy);
        blood3.setTravelRange(40f);
        actor.world.spawnEntity(blood3);
    }
}
