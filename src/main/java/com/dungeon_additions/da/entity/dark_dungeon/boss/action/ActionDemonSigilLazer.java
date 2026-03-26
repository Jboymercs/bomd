package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkBase;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.packets.MessageDirectionForRender;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class ActionDemonSigilLazer implements IMultiAction {
    private EntityDarkBase entity;
    private boolean isShootingLazer;

    double maxLaserDistance;
    private final float laserExplosionSize;
    int beamLag;
    private final byte stopLaserByte;
    private final Consumer<Vec3d> onLaserImpact;
    private float damageIn;
    public ActionDemonSigilLazer(EntityDarkBase actor, byte stopLaserByte, Consumer<Vec3d> onLaserImpact, float damageIn) {
        this.entity = actor;
        this.laserExplosionSize = 0;
        this.maxLaserDistance = 18;
        this.beamLag = 1;
        this.stopLaserByte = stopLaserByte;
        this.onLaserImpact = onLaserImpact;
        this.damageIn = damageIn;
    }
    @Override
    public void doAction() {
        int chargeUpTime = 10;
        int lazerEndTime = 190;
        for (int i = 0; i < chargeUpTime; i++) {
            entity.addEvent(() -> entity.world.setEntityState(entity, ModUtils.PARTICLE_BYTE), i);
        }
        entity.addEvent(()-> this.isShootingLazer = true, chargeUpTime);

        entity.addEvent(()-> {
            this.isShootingLazer = false;

            entity.addEvent(() -> entity.world.setEntityState(entity, stopLaserByte), beamLag + 1);
        }, lazerEndTime);
    }


    private Vec3d onLaserImpact(RayTraceResult raytraceresult) {
        Vec3d hitPos = raytraceresult.hitVec;
        onLaserImpact.accept(hitPos);

        return hitPos;
    }


    @Override
    public void update() {
        if(this.isShootingLazer) {
            Vec3d lookPos = this.entity.getLookVec();
            Vec3d currentDirAndPosition = new Vec3d(entity.posX + lookPos.x, entity.posY + 0.25, entity.posZ + lookPos.z);

            entity.addEvent(() -> {

                // Extend shooting beyond the target position up to 40 blocks
                Vec3d laserDirection = lookPos.scale(10.0D).normalize();
                Vec3d lazerPos = currentDirAndPosition.add(laserDirection.scale(maxLaserDistance));
                // Ray trace both blocks and entities
                RayTraceResult raytraceresult = entity.world.rayTraceBlocks(entity.getPositionVector().add(ModUtils.yVec(0.5)), lazerPos, false, true, false);
                if (raytraceresult != null) {
                    lazerPos = onLaserImpact(raytraceresult);
                }

                for (Entity target : ModUtils.findEntitiesInLine(entity.getPositionVector().add(ModUtils.yVec(0.5)), lazerPos, entity.world, entity)) {
                    DamageSource source = ModDamageSource.builder()
                            .directEntity(entity)
                            .stoppedByArmorNotShields()
                            .type(ModDamageSource.MAGIC).disablesShields()
                            .build();
                    //Damage
                    if(!(target instanceof EntityDarkBase)) {
                        target.attackEntityFrom(source, (float) (damageIn));
                    }
                }

                Main.network.sendToAllTracking(new MessageDirectionForRender(entity, lazerPos), entity);
            }, beamLag);
        }
    }



    @Override
    public int attackLength() {
        return 200;
    }
}
