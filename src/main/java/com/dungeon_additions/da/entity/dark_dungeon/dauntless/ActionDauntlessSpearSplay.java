package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class ActionDauntlessSpearSplay implements IActionDauntless {
    @Override
    public void performAction(EntityDauntless actor, EntityLivingBase target) {
        if (actor.isRangedMode()) {
            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1.5, 0, 0)));

            Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

            Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, ModRand.range(-70, 70)).normalize().scale(8);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);
            ModUtils.lineCallback(lineStart, lineEnd, 9, (pos, i) -> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                float speed = (float) 1.3;
                missile.rotationPitch = actor.rotationPitch;
                missile.rotationYaw = actor.rotationYaw;
                actor.world.spawnEntity(missile);
                ModUtils.throwProjectileNoSpawn(pos, missile, 0F, speed);
            });
        } else {
            float speed = (float) 0.9;

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw;
                missile.shoot(actor, 25, actor.rotationYaw, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw + 25;
                missile.shoot(actor, 25, actor.rotationYaw + 25, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw - 25;
                missile.shoot(actor, 25, actor.rotationYaw - 25, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw - 50;
                missile.shoot(actor, 25, actor.rotationYaw - 50, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw + 50;
                missile.shoot(actor, 25, actor.rotationYaw + 50, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw - 75;
                missile.shoot(actor, 25, actor.rotationYaw - 75, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);

            actor.addEvent(()-> {
                ProjectileDauntlessSlice missile = new ProjectileDauntlessSlice(actor.world, actor, (float) (actor.getAttack() * 0.8));
                Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 1.7, 0)));
                missile.setPosition(relPos.x, relPos.y, relPos.z);
                missile.rotationPitch = 25;
                missile.rotationYaw = actor.rotationYaw + 75;
                missile.shoot(actor, 25, actor.rotationYaw + 75, 0.0F, speed, 0.0F);
                actor.world.spawnEntity(missile);
            }, 1);
        }
    }
}
