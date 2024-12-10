package com.dungeon_additions.da.entity.night_lich.action;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.night_lich.*;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ActionLichRageMode implements IActionLich{


    @Override
    public void performAction(EntityNightLich actor, EntityLivingBase target) {
        //spam some Projectiles
        for(int i = 0; i <= 100; i += 25) {
            actor.addEvent(()-> {
                if(actor.world.rand.nextInt(3) == 0) {
                    ProjectileTrackingMagicMissile missile_1 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);
                    ProjectileTrackingMagicMissile missile_2 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);
                    ProjectileTrackingMagicMissile missile_3 = new ProjectileTrackingMagicMissile(actor.world, actor, 10F, target);

                    Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,5.5,-1)));
                    missile_1.setTravelRange(80F);
                    missile_1.setPosition(relPos.x, relPos.y, relPos.z);
                    actor.world.spawnEntity(missile_1);
                    actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));

                    actor.addEvent(()-> {
                        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,1.5,-4)));
                        missile_2.setTravelRange(80F);
                        missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
                        actor.world.spawnEntity(missile_2);
                        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));
                    }, 5);

                    actor.addEvent(()-> {
                        Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0,1.5,4)));
                        missile_3.setTravelRange(80F);
                        missile_3.setPosition(relPos2.x, relPos2.y, relPos2.z);
                        actor.world.spawnEntity(missile_3);
                        actor.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 2.0F, 0.8F / (new Random().nextFloat() * 0.4F + 0.6F));
                    }, 10);
                } else {
                    ProjectileMagicMissile missile = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    ProjectileMagicMissile missile_2 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    ProjectileMagicMissile missile_3 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    ProjectileMagicMissile missile_4 = new ProjectileMagicMissile(actor.world, actor,(float) (actor.getAttack()));
                    ProjectileMagicMissile missile_5 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    ProjectileMagicMissile missile_6 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    ProjectileMagicMissile missile_7 = new ProjectileMagicMissile(actor.world, actor, (float) (actor.getAttack()));
                    Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,0)));
                    Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,0.8)));
                    Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,-0.8)));
                    Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,-1.6)));
                    Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,1.6)));
                    Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,-2.4)));
                    Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2,1.5,2.4)));
                    missile.setPosition(relPos.x, relPos.y, relPos.z);
                    missile_2.setPosition(relPos2.x, relPos2.y, relPos2.z);
                    missile_3.setPosition(relPos3.x, relPos3.y, relPos3.z);
                    missile_4.setPosition(relPos4.x, relPos4.y, relPos4.z);
                    missile_5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                    missile_6.setPosition(relPos6.x, relPos6.y, relPos6.z);
                    missile_7.setPosition(relPos7.x, relPos7.y, relPos7.z);

                    actor.world.spawnEntity(missile);
                    actor.world.spawnEntity(missile_2);
                    actor.world.spawnEntity(missile_3);
                    actor.world.spawnEntity(missile_4);
                    actor.world.spawnEntity(missile_5);
                    actor.world.spawnEntity(missile_6);
                    actor.world.spawnEntity(missile_7);
                    missile.setTravelRange(40);
                    missile_2.setTravelRange(40);
                    missile_3.setTravelRange(40);
                    missile_4.setTravelRange(40);
                    missile_5.setTravelRange(40);
                    missile_6.setTravelRange(40);
                    missile_7.setTravelRange(40);

                    actor.addEvent(()-> {
                        actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 0.8)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_2,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -0.8)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_3,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -1.6)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_4,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 1.6)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_5,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, -2.4)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_6,0F, speed);
                        });
                    }, 1);

                    actor.addEvent(()-> {
                        Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.5, 2.4)));

                        Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                        Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                        Vec3d lineStart = targetPos.subtract(lineDir);
                        Vec3d lineEnd = targetPos.add(lineDir);

                        float speed = (float) MobConfig.magic_missile_velocity;

                        ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, t) -> {
                            ModUtils.throwProjectileNoSpawn(pos,missile_7,0F, speed);
                        });
                    }, 1);
                }
            }, i);
        }


        for(int c = 0; c <= 75; c += 25) {
            actor.addEvent(()-> {
                if(actor.world.rand.nextInt(3) == 0) {
                    actor.addEvent(()-> {
                        EntityLichStaffAOE staff_1 = new EntityLichStaffAOE(actor.world, true);
                        Vec3d posToo = target.getPositionVector().add(ModRand.range(-4, 4), 0, ModRand.range(-4, 4));
                        int y2 = getSurfaceHeight(actor.world, new BlockPos(posToo.x, 0, posToo.z), (int) posToo.x - 6, (int) posToo.z + 3);
                        staff_1.setPosition(posToo.x, y2, posToo.z);
                        actor.world.spawnEntity(staff_1);
                    }, 1);
                } else {
                    actor.addEvent(() -> {
                        BlockPos randIPos = new BlockPos(target.posX + ModRand.range(-10, 10), target.posY, target.posZ + ModRand.range(-10, 10));
                        int y = getSurfaceHeight(actor.world, new BlockPos(randIPos.getX(), 0, randIPos.getZ()), (int) target.posY - 2, (int) target.posY + 4);
                        EntityLivingBase new_mob = new EntityLichSpawn(actor.world, actor);
                        new_mob.setPosition(randIPos.getX() + 0.5, y + 1, randIPos.getZ() + 0.5);
                        actor.world.spawnEntity(new_mob);
                    }, 1);
                }
            }, c);
        }
    }



    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
