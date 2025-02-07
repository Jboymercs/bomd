package com.dungeon_additions.da.entity.sky_dungeon.gargoyle;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.sky_dungeon.EntityFarumSpike;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionGargoyleShoot implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.7f;
        float pitch = 0;
        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw - 45, 0.0F, speed, inaccuracy);
        }, 1);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw, 0.0F, speed, inaccuracy);
        }, 2);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw + 45, 0.0F, speed, inaccuracy);
        }, 3);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw + 90, 0.0F, speed, inaccuracy);
        }, 4);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw + 135, 0.0F, speed, inaccuracy);
        }, 5);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw + 180, 0.0F, speed, inaccuracy);
        }, 6);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw - 135, 0.0F, speed, inaccuracy);
        }, 7);

        actor.addEvent(()-> {
            Vec3d Pos = actor.getPositionVector().add(0, 1.5, 0);
            EntityFarumSpike dart = new EntityFarumSpike(actor.world, actor);
            dart.setPosition(Pos.x, Pos.y, Pos.z);
            actor.world.spawnEntity(dart);
            dart.shoot(actor, pitch, actor.rotationYaw - 90, 0.0F, speed, inaccuracy);
        }, 8);
    }
}
