package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionPlayerShootSpead implements IActionPlayer{


    @Override
    public void performAction(EntityPlayer actor) {

        double additionalDamage = 0.2;
        if(actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
            additionalDamage = 0.45;
        }

        ProjectileMagicMissile missile = new ProjectileMagicMissile(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage));
        ProjectileMagicMissile missile_2 = new ProjectileMagicMissile(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage));
        ProjectileMagicMissile missile_3 = new ProjectileMagicMissile(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage));
        ProjectileMagicMissile missile_4 = new ProjectileMagicMissile(actor.world, actor,(float) (MobConfig.night_lich_attack_damage * additionalDamage));
        ProjectileMagicMissile missile_5 = new ProjectileMagicMissile(actor.world, actor, (float) (MobConfig.night_lich_attack_damage * additionalDamage));

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,3.0,0)));
        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,2.8,0.8)));
        Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,2.8,-0.8)));
        Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,2.6,-1.6)));
        Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0,2.6,1.6)));

        missile.setPosition(relPos.x, relPos.y, relPos.z);
        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
        missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
        missile_4.setPosition(relPos4.x, relPos4.y, relPos4.z);
        missile_5.setPosition(relPos5.x, relPos5.y, relPos5.z);

        actor.world.spawnEntity(missile);
        actor.world.spawnEntity(missile_2);
        actor.world.spawnEntity(missile_3);
        actor.world.spawnEntity(missile_4);
        actor.world.spawnEntity(missile_5);
        missile.setTravelRange(40);
        missile_2.setTravelRange(40);
        missile_3.setTravelRange(40);
        missile_4.setTravelRange(40);
        missile_5.setTravelRange(40);

        float speed = (float) (MobConfig.magic_missile_velocity - 0.3);
        missile.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
        missile_2.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
        missile_3.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
        missile_4.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
        missile_5.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0, speed, 0F);
    }
}
