package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionDauntlessShootCrystal implements IActionDauntless{
    @Override
    public void performAction(EntityDauntless actor, EntityLivingBase target) {
        Vec3d targetedPos = target.getPositionVector();
        Vec3d actorPos = actor.getPositionVector();

        if(actor.isRangedMode()) {
            ProjectileDauntlessCrystal fireBall = new ProjectileDauntlessCrystal(actor.world, actor, actor.getAttack());
            fireBall.setNoGravity(true);
            actor.world.spawnEntity(fireBall);
            ModUtils.throwProjectileNoSpawn(targetedPos, fireBall, 0, (float) 1);
            actor.playSound(SoundsHandler.DAUNTLESS_SHOOT_CRYSTAL, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));

            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, 1.5)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 30);

            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 2, 2)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 50);

            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1, 1.5)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 70);


            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, -1.5)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 90);

            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 2, -2)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 110);

            actor.addEvent(() -> {
                Vec3d relpos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1, -1.5)));
                new ActionSummonDelayedLazer(relpos).performAction(actor, target);
            }, 130);
        } else {
            actor.playSound(SoundsHandler.DAUNTLESS_SHOOT_CRYSTAL, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            //creates a 5 wave delayed lazer attack
            actor.addEvent(()-> {
                Vec3d relPos = actorPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.5, 0)));
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (actor.getAttack() * 0.5), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 1);

            actor.addEvent(()-> {
                Vec3d relPos = actorPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.5, 1)));
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 1)));
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (actor.getAttack() * 0.5), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 1);

            actor.addEvent(()-> {
                Vec3d relPos = actorPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.5, 2)));
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 2)));
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (actor.getAttack() * 0.5), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 1);

            actor.addEvent(()-> {
                Vec3d relPos = actorPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.5, -1)));
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -1)));
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (actor.getAttack() * 0.5), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 1);

            actor.addEvent(()-> {
                Vec3d relPos = actorPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.5, -2)));
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -2)));
                //We want to ensure the offset is in place. so target will remain null
                EntityDelayedLazer lazer = new EntityDelayedLazer(actor.world, 15, targetPos, actor, (float) (actor.getAttack() * 0.5), null);
                lazer.setPosition(relPos.x, relPos.y, relPos.z);
                actor.world.spawnEntity(lazer);
            }, 1);
        }

    }
}
