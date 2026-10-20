package com.dungeon_additions.da.entity.dark_dungeon.minion_action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class ActionCreepLaser implements IMultiAction {
    private EntityAbstractBase entity;
    private boolean isShootingLazer;

    double maxLaserDistance;
    int beamLag;
    private Vec3d teleportPosition;
    private final byte stopLaserByte;
    private final Consumer<Vec3d> onLaserImpact;

    public ActionCreepLaser(EntityAbstractBase actorIn, byte stopLaserByte, Consumer<Vec3d> onLaserImpact) {
        this.entity = actorIn;
        this.maxLaserDistance = 40;
        this.beamLag = 3;
        this.stopLaserByte = stopLaserByte;
        this.onLaserImpact = onLaserImpact;
    }

    @Override
    public void doAction() {
        int chargeUpTime = 20;
        int lazerEndTime = 80;

        for (int i = 0; i < chargeUpTime; i++) {
            entity.addEvent(() -> entity.world.setEntityState(entity, ModUtils.PARTICLE_BYTE), i);
        }
        //turns on lazer
        entity.addEvent(()-> this.isShootingLazer = true, chargeUpTime);
        entity.addEvent(()-> this.entity.playSound(SoundsHandler.VOIDCLYSM_BEAM_TELEPORT, 3.5F, 1.0F), chargeUpTime);

        //end lazer and teleport to predicted location
        entity.addEvent(()-> {
            this.isShootingLazer = false;
            //do explosion attack
            entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
        }, lazerEndTime);
    }

    @Override
    public void update() {
        if(this.isShootingLazer) {
            if(entity.getAttackTarget() != null) {
                Vec3d laserShootPos = entity.getAttackTarget().getPositionVector();
                entity.addEvent(() -> {

                    // Extend shooting beyond the target position up to 40 blocks
                    Vec3d laserDirection = laserShootPos.subtract(entity.getPositionEyes(1)).normalize();
                    Vec3d lazerPos = laserShootPos.add(laserDirection.scale(maxLaserDistance));
                    // Ray trace both blocks and entities
                    RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionEyes(1), lazerPos, false, true, false);
                    if (raytraceresult != null) {
                        lazerPos = onLaserImpact(raytraceresult);
                    }

                    for (Entity target : ModUtils.findEntitiesInLine(entity.getPositionEyes(1), lazerPos, entity.world, entity)) {
                        DamageSource source = ModDamageSource.builder()
                                .directEntity(entity)
                                .stoppedByArmorNotShields()
                                .type(ModDamageSource.MAGIC).disablesShields()
                                .build();
                        target.attackEntityFrom(source, (float) (entity.getAttack() * 0.6));
                    }

                    ModUtils.addEntityVelocity(entity, laserDirection.scale(-0.03f));

                    Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
                }, beamLag);
            }
            else {
                // Prevent the gauntlet from instantly locking onto other targets with the lazer.
                isShootingLazer = false;
                entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
            }

        }
    }

    private Vec3d onLaserImpact(RayTraceResult raytraceresult) {
        Vec3d hitPos = raytraceresult.hitVec;
        onLaserImpact.accept(hitPos);
        if(entity.ticksExisted % 2 == 0) {
            ModUtils.destroyBlocksInAABBWyrk(ModUtils.vecBox(hitPos, hitPos).grow(0.25), entity.world, entity);
        }
        return hitPos;
    }


    @Override
    public int attackLength() {
        return 80;
    }
}
