package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionSummonDelayedLazer implements IActionDauntless{
    private Vec3d pos;

    public ActionSummonDelayedLazer(Vec3d pos) {
        this.pos = pos;
    }

    @Override
    public void performAction(EntityDauntless actor, EntityLivingBase target) {
        if(target != null) {
            EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, target.getPositionVector(), actor, (float) (actor.getAttack() * 0.5), target);
            lazer.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(lazer);
        }
    }
}
