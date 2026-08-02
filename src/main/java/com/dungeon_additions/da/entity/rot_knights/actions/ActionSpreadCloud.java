package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.rot_knights.ProjectileDelayedPoisonCloud;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionSpreadCloud implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        float inaccuracy = 0.0f;
        float speed = 0.25f;
        float pitch = 22;

        for(int i = 0; i < 190; i+= 10) {
            actor.addEvent(()-> {
                ProjectileDelayedPoisonCloud cloud = new ProjectileDelayedPoisonCloud(actor.world, actor, (float) actor.getAttack() * 0.75F, 30);
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0.5, 1.4, 0)));
                cloud.setPosition(relPos.x, relPos.y, relPos.z);
                cloud.shoot(actor, pitch, actor.rotationYaw + (ModRand.range(1, 360) + ModRand.randSign()), 0.0F, speed, inaccuracy);
                actor.world.spawnEntity(cloud);
                Random rand = new Random();
                actor.playSound(SoundsHandler.VOLACTILE_SHOOT_CANNON, 0.4f, 1.5f / (rand.nextFloat() * 0.4F + 0.4f));
            }, i);
        }
    }
}
