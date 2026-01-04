package com.dungeon_additions.da.entity.gaelon_dungeon.apathyr;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.frost_dungeon.great_wyrk.IMultiAction;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action.ActionKingStomp;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ActionSuperDash implements IMultiAction {

    private boolean preppingTeleport = false;
    int delayed_prediction;
    private EntityAbstractBase actor;
    private Vec3d teleportPosition;

    public ActionSuperDash(EntityAbstractBase actor) {
        this.actor = actor;
        this.delayed_prediction = 1;
    }

    private int timerTick = 0;
    @Override
    public void doAction() {
        int teleportTime = 35;
        this.preppingTeleport = true;
        this.timerTick = 0;

        actor.addEvent(()-> {
            this.preppingTeleport = false;
        }, teleportTime - 3);
        //teleports boss at given time
        actor.addEvent(()-> {
            if(this.teleportPosition != null) {
                //try to find ground at the given coords
                int setYValue = ModUtils.getSurfaceHeightGeneral(actor.world, new BlockPos(teleportPosition.x, teleportPosition.y, teleportPosition.z), (int) teleportPosition.y - 3, (int) teleportPosition.y + 2);
                this.actor.setPosition(teleportPosition.x, setYValue + 1, teleportPosition.z);
            }
            this.actor.playSound(SoundsHandler.APATHYR_SWING, 1.5F, 0.9f + ModRand.getFloat(0.5f));
            actor.addEvent(()-> {
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MOB)
                        .directEntity(this.actor)
                        .stoppedByArmorNotShields().disablesShields().build();

                ModUtils.handleAreaImpact(4, (e) -> this.actor.getAttack(), this.actor, this.actor.getPositionVector().add(ModUtils.getRelativeOffset(actor, new Vec3d(2.5, 0, 0))), source);
                actor.playSound(SoundsHandler.APATHYR_AXE_IMPACT, 1.5f, 0.7f / (new Random().nextFloat() * 0.4f + 0.2f));
            }, 12);
        },teleportTime);
    }

    @Override
    public void update() {
        timerTick++;

        if(this.preppingTeleport) {
            if(actor.getAttackTarget() != null) {
                //stop the teleport prepping 10 ticks before teleporting
                if(!this.preppingTeleport && this.teleportPosition != null) {
                    if(timerTick % 5 == 0) {
                        Main.proxy.spawnParticle(16,actor.world, teleportPosition.x, teleportPosition.y + 1, teleportPosition.z, 0, 0, 0, 20);
                    }
                    //locate the position and guestimate player tracking
                } else {
                    actor.addEvent(()-> {
                        if(actor.getAttackTarget() != null) {
                            Vec3d laserShootPos = actor.getAttackTarget().getPositionVector();
                            actor.addEvent(()-> {
                                if(actor.getAttackTarget() != null) {
                                    this.teleportPosition = laserShootPos;
                                    if(timerTick % 10 == 0) {
                                        Main.proxy.spawnParticle(16,actor.world, teleportPosition.x, teleportPosition.y + 1, teleportPosition.z, 0, 0, 0, 20);
                                    }
                                }
                            }, 4);
                        }
                    }, delayed_prediction);
                }
            }
        }
    }
}
