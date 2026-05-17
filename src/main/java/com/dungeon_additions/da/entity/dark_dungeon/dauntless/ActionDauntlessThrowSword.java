package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionDauntlessThrowSword implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionVector();
            actor.addEvent(()-> {
                Vec3d targetedPos = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                actor.world.spawnEntity(sword);
            }, 3);
        }, 1);


        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionVector();
            actor.addEvent(()-> {
                Vec3d targetedPos = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                actor.world.spawnEntity(sword);
            }, 3);
        }, 60);


        actor.addEvent(()-> {
            Vec3d targetPos = target.getPositionVector();
            actor.addEvent(()-> {
                Vec3d targetedPos = target.getPositionVector();
                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                actor.world.spawnEntity(sword);
            }, 3);
        }, 120);
    }
}
