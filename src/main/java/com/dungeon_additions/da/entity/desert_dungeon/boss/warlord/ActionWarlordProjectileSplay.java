package com.dungeon_additions.da.entity.desert_dungeon.boss.warlord;

import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileDesertOrb;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionWarlordProjectileSplay implements IAction {

    private boolean isOffhand;

    public ActionWarlordProjectileSplay(boolean isOffhand) {
        this.isOffhand = isOffhand;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        if (isOffhand) {
            ProjectileDesertOrb missile = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_2 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_3 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_4 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_5 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_6 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_7 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));

            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 1.5, 0)));

            Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 2.25, 0.75)));
            Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3, 1.5)));
            Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3.75, 2.25)));

            Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0.75, -0.75)));
            Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0, -1.5)));
            Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, -0.75, -2.25)));

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

            missile.setTravelRange(60);
            missile_2.setTravelRange(60);
            missile_3.setTravelRange(60);
            missile_4.setTravelRange(60);
            missile_5.setTravelRange(60);
            missile_6.setTravelRange(60);
            missile_7.setTravelRange(60);

            for (int i = 0; i < 15; i++) {
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 1.5, 0)));
                    ModUtils.setEntityPosition(missile, pos);
                }, i);

                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 2.25, 0.75)));
                    ModUtils.setEntityPosition(missile_2, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3, 1.5)));
                    ModUtils.setEntityPosition(missile_3, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3.75, 2.25)));
                    ModUtils.setEntityPosition(missile_4, pos);
                }, i);

                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0.75, -0.75)));
                    ModUtils.setEntityPosition(missile_5, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0, -1.5)));
                    ModUtils.setEntityPosition(missile_6, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, -0.75, -2.25)));
                    ModUtils.setEntityPosition(missile_7, pos);
                }, i);
            }

            actor.addEvent(() -> {
                actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F);

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, 0.75)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_2, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 2.25, 1.5)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_3, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, 2.25)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_4, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.75, -0.75)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_5, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1.5)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_6, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, -2.25)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_7, 0F, speed);
                });
            }, 16);
        } else {
            ProjectileDesertOrb missile = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_2 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_3 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_4 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_5 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_6 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));
            ProjectileDesertOrb missile_7 = new ProjectileDesertOrb(actor.world, actor, (float) (actor.getAttack() * 0.8));

            Vec3d relPos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 1.5, 0)));

            Vec3d relPos2 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 2.25, -0.75)));
            Vec3d relPos3 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3, -1.5)));
            Vec3d relPos4 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3.75, -2.25)));

            Vec3d relPos5 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0.75, 0.75)));
            Vec3d relPos6 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0, 1.5)));
            Vec3d relPos7 = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, -0.75, 2.25)));

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

            missile.setTravelRange(60);
            missile_2.setTravelRange(60);
            missile_3.setTravelRange(60);
            missile_4.setTravelRange(60);
            missile_5.setTravelRange(60);
            missile_6.setTravelRange(60);
            missile_7.setTravelRange(60);

            for (int i = 0; i < 15; i++) {
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 1.5, 0)));
                    ModUtils.setEntityPosition(missile, pos);
                }, i);

                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 2.25, -0.75)));
                    ModUtils.setEntityPosition(missile_2, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3, -1.5)));
                    ModUtils.setEntityPosition(missile_3, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 3.75, -2.25)));
                    ModUtils.setEntityPosition(missile_4, pos);
                }, i);

                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0.75, 0.75)));
                    ModUtils.setEntityPosition(missile_5, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, 0, 1.5)));
                    ModUtils.setEntityPosition(missile_6, pos);
                }, i);
                actor.addEvent(() -> {
                    Vec3d pos = actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.0, -0.75, 2.25)));
                    ModUtils.setEntityPosition(missile_7, pos);
                }, i);
            }

            actor.addEvent(() -> {
                actor.playSound(SoundsHandler.LICH_SHOOT_MISSILE, 2.0f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F);

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 1.5, -0.75)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_2, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 2.25, -1.5)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_3, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 3, -2.25)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_4, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0.75, 0.75)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_5, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1.5)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_6, 0F, speed);
                });
            }, 16);

            actor.addEvent(() -> {
                float speed = (float) 1;
                Vec3d targetPos = target.getPositionEyes(1.0F).add(ModUtils.getRelativeOffset(actor, new Vec3d(0, -0.75, 2.25)));

                Vec3d fromTargetTooActor = actor.getPositionVector().subtract(targetPos);
                Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(1);
                Vec3d lineStart = targetPos.subtract(lineDir);
                Vec3d lineEnd = targetPos.add(lineDir);

                ModUtils.lineCallback(lineStart, lineEnd, 1, (pos, i) -> {
                    ModUtils.throwProjectileNoSpawn(pos, missile_7, 0F, speed);
                });
            }, 16);
        }
    }
}
