package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicGround;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.Vec3d;

public class ActionPlayerGroundMissiles implements IActionPlayer{
    @Override
    public void performAction(EntityPlayer actor) {
        float inaccuracy = 0.0f;
        float speed = 0.55f;
        float pitch = 0; // Projectiles aim straight ahead always

        double additionalDamage = 0.35;
        if(actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
            additionalDamage = 0.65;
        }

        ProjectileMagicGround missile = new ProjectileMagicGround(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage), null, actor);
        ProjectileMagicGround missile_2 = new ProjectileMagicGround(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage), null, actor);
        ProjectileMagicGround missile_3 = new ProjectileMagicGround(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage), null, actor);
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 0)));
        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        missile.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile_2.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile_3.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        missile.setTravelRange(12f);
        missile_2.setTravelRange(12f);
        missile_3.setTravelRange(12f);
        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile_2);
        actor.world.spawnEntity(missile_3);
    }
}
