package com.dungeon_additions.da.entity.desert_dungeon.boss.warlord;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertStorm;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionWarlordSummonStorms implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        actor.addEvent(()-> {
            ProjectileDesertStorm storm = new ProjectileDesertStorm(actor.world, actor, actor.getAttack(), null);
            Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
            storm.setPosition(relPos3.x, relPos3.y, relPos3.z);
            storm.shoot(actor, 0, actor.rotationYaw - 45, 0.0F, 0.6F, 0);
            storm.setTravelRange(16);
            actor.world.spawnEntity(storm);
        }, 1);
        actor.addEvent(()-> {
            ProjectileDesertStorm storm = new ProjectileDesertStorm(actor.world, actor, actor.getAttack(), null);
            Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
            storm.setPosition(relPos3.x, relPos3.y, relPos3.z);
            storm.shoot(actor, 0, actor.rotationYaw, 0.0F, 0.6F, 0);
            storm.setTravelRange(16);
            actor.world.spawnEntity(storm);
        }, 2);
        actor.addEvent(()-> {
            ProjectileDesertStorm storm = new ProjectileDesertStorm(actor.world, actor, actor.getAttack(), null);
            Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
            storm.setPosition(relPos3.x, relPos3.y, relPos3.z);
            storm.shoot(actor, 0, actor.rotationYaw + 45, 0.0F, 0.6F, 0);
            storm.setTravelRange(16);
            actor.world.spawnEntity(storm);
        }, 3);
    }
}
