package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.night_lich.ProjectileTrackingMagicMissile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionShootTrackingProjectiles implements IActionLich{


    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        ProjectileTrackingMagicMissile missile_1 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);
        ProjectileTrackingMagicMissile missile_2 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);
        ProjectileTrackingMagicMissile missile_3 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);

        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,0,-1)));
        missile_1.setTravelRange(80F);
        missile_1.setPosition(relPos.x, relPos.y, relPos.z);
        actor.world.spawnEntity(missile_1);
        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));

        actor.addEvent(()-> {
            Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,0,0)));
            missile_2.setTravelRange(80F);
            missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
            actor.world.spawnEntity(missile_2);
            actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));
        }, 5);

        actor.addEvent(()-> {
            Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,0,0)));
            missile_3.setTravelRange(80F);
            missile_3.setPosition(relPos2.x, relPos2.y, relPos2.z);
            actor.world.spawnEntity(missile_3);
            actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));
        }, 10);

    }
}
