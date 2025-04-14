package com.dungeon_additions.da.entity.flame_knight.bareant;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.ProjectileTrackingFlame;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ActionSummonBareantFlame implements IAction {

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        ProjectileTrackingFlame flame = new ProjectileTrackingFlame(actor.world, actor, 9, target);
        Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1,0.5,0)));
        flame.setPosition(relPos.x, relPos.y, relPos.z);
        actor.world.spawnEntity(flame);
        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 0.6f / (actor.world.rand.nextFloat() * 0.4f + 0.2f));
        flame.setTravelRange(30);
        //Hold the flame and then let it go
        for(int i = 0; i < 15; i++) {
            actor.addEvent(()-> ModUtils.setEntityPosition(flame, relPos), i);
        }
    }
}
