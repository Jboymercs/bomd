package com.dungeon_additions.da.entity.void_dungeon.voidclysm_action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class ActionLazerTeleport implements IMultiAction {
    private EntityAbstractBase entity;
    private boolean isShootingLazer;

    private boolean holdLazer;
    double maxLaserDistance;
    int beamLag;
    private Vec3d teleportPosition;
    private final byte stopLaserByte;
    private final Consumer<Vec3d> onLaserImpact;

    public ActionLazerTeleport(EntityAbstractBase actorIn, byte stopLaserByte, Consumer<Vec3d> onLaserImpact) {
        this.entity = actorIn;
        this.maxLaserDistance = 40;
        this.beamLag = 3;
        this.stopLaserByte = stopLaserByte;
        this.onLaserImpact = onLaserImpact;
    }

    @Override
    public void doAction() {
        int chargeUpTime = 10;
        int lazerEndTime = 70;

        for (int i = 0; i < chargeUpTime; i++) {
            entity.addEvent(() -> entity.world.setEntityState(entity, ModUtils.PARTICLE_BYTE), i);
        }
        //turns on lazer
        entity.addEvent(()-> this.isShootingLazer = true, chargeUpTime);

        entity.addEvent(() -> this.holdLazer = true, chargeUpTime + 40);

        //end lazer and teleport to predicted location
        entity.addEvent(()-> {
            this.isShootingLazer = false;
            this.holdLazer = false;
            //do explosion attack
            entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
            this.entity.setPosition(teleportPosition.x, teleportPosition.y, teleportPosition.z);
            this.entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 2.0F, 1.0F);
            entity.addEvent(()-> {
                if(this.teleportPosition != null) {
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.EXPLOSION)
                            .directEntity(this.entity)
                            .stoppedByArmorNotShields().disablesShields().build();

                    ModUtils.handleAreaImpact(5, (e) -> this.entity.getAttack(), this.entity, this.entity.getPositionVector().add(ModUtils.yVec(1)), source);
                    this.entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f + ModRand.getFloat(0.1f));
                    this.entity.world.setEntityState(this.entity, ModUtils.THIRD_PARTICLE_BYTE);
                }
            }, 5);
        }, lazerEndTime);
    }

    @Override
    public void update() {
        if(this.isShootingLazer) {
            if(entity.getAttackTarget() != null) {
                if(this.holdLazer && this.teleportPosition != null) {
                    entity.addEvent(() -> {
                        // Extend shooting beyond the target position up to 40 blocks
                        Vec3d laserDirection = teleportPosition.subtract(entity.getPositionEyes(1)).normalize();
                        Vec3d lazerPos = teleportPosition.add(laserDirection.scale(maxLaserDistance));
                        // Ray trace both blocks and entities
                        RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionEyes(1).subtract(0, -1.5, 0), lazerPos, false, true, false);
                        if (raytraceresult != null) {
                            lazerPos = onLaserImpact(raytraceresult);
                        }
                        ModUtils.addEntityVelocity(entity, laserDirection.scale(-0.03f));

                        Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
                    }, 1);
                } else {
                    //adjust lazer
                    entity.addEvent(() -> {
                        Vec3d laserShootPos = entity.getAttackTarget().getPositionVector();
                        if(entity.getAttackTarget() != null) {
                            entity.addEvent(() -> {
                                Vec3d targetedPos = entity.getAttackTarget().getPositionVector();
                                if(entity.getAttackTarget() != null) {
                                    //predictionPosition calculates a few movements ahead of the player for where the lazer will impact
                                    Vec3d predictedPosition = ModUtils.predictPlayerPosition(laserShootPos, targetedPos, 4);
                                    this.teleportPosition = predictedPosition.add(0, 0.5, 0);
                                    // Extend shooting beyond the target position up to 40 blocks
                                    Vec3d laserDirection = predictedPosition.subtract(entity.getPositionEyes(1)).normalize();
                                    Vec3d lazerPos = predictedPosition.add(laserDirection.scale(maxLaserDistance));
                                    // Ray trace both blocks and entities
                                    RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionEyes(1).subtract(0, -1.5, 0), lazerPos, false, true, false);
                                    if (raytraceresult != null) {
                                        lazerPos = onLaserImpact(raytraceresult);
                                    }


                                    ModUtils.addEntityVelocity(entity, laserDirection.scale(-0.03f));

                                    Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
                                }
                            }, beamLag);
                        }
                    }, 1);
                }
            }
            else {
                // Prevent the gauntlet from instantly locking onto other targets with the lazer.
                isShootingLazer = false;
                holdLazer = false;
                entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
            }

        }
    }

    private Vec3d onLaserImpact(RayTraceResult raytraceresult) {
        Vec3d hitPos = raytraceresult.hitVec;
        onLaserImpact.accept(hitPos);

        return hitPos;
    }


    @Override
    public int attackLength() {
        return 100;
    }
}
