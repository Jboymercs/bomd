package com.dungeon_additions.da.entity.flame_knight.misc;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;

public class ActionReleaseFlames implements IAction {

    private int time;
    public ActionReleaseFlames(int time) {
    this.time = time;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        for(int i = 0; i <= time; i += 5) {
            actor.addEvent(()-> {
                ProjectileTrackingFlame newFlame = new ProjectileTrackingFlame(actor.world, actor, 18, target, true);
                actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.6f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));
                newFlame.setPosition(actor.posX, actor.posY + 2.7, actor.posZ);
                actor.world.spawnEntity(newFlame);
                newFlame.setTravelRange(40);
            }, i);
        }
    }
}
