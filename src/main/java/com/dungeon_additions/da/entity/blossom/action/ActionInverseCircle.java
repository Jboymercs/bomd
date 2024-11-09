package com.dungeon_additions.da.entity.blossom.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionInverseCircle implements IAction {
    /**
     * Attack Pattern switch up with one of the attacks, blossom will now summon on Inverse
     * @param actor
     * @param target
     */
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(3, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(4, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(5, 28, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(6, 32, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(7, 36, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityVoidSpike spike = new EntityVoidSpike(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
    }
}
