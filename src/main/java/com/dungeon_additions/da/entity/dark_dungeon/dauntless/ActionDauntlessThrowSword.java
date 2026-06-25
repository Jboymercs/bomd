package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ActionDauntlessThrowSword implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {

        List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(40D), e -> !e.getIsInvulnerable());

        //summon swords for each player
        if(!nearbyPlayers.isEmpty()) {
            for(EntityPlayer player : nearbyPlayers) {
                actor.addEvent(()-> {
                    if(player != null) {
                        Vec3d targetPos = player.getPositionVector();
                        actor.addEvent(() -> {
                            Vec3d targetedPos = player.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                            EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                            sword.setPosition(predictedPosition.x, player.posY + 10, predictedPosition.z);
                            actor.world.spawnEntity(sword);
                        }, 3);
                    }
                }, 1);


                actor.addEvent(()-> {
                    if(player != null) {
                        Vec3d targetPos = player.getPositionVector();
                        actor.addEvent(() -> {
                            Vec3d targetedPos = player.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                            EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                            sword.setPosition(predictedPosition.x, player.posY + 10, predictedPosition.z);
                            actor.world.spawnEntity(sword);
                        }, 3);
                    }
                }, 60);


                actor.addEvent(()-> {
                    if(player != null) {
                        Vec3d targetPos = player.getPositionVector();
                        actor.addEvent(() -> {
                            Vec3d targetedPos = player.getPositionVector();
                            Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                            EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                            sword.setPosition(predictedPosition.x, player.posY + 10, predictedPosition.z);
                            actor.world.spawnEntity(sword);
                        }, 3);
                    }
                }, 120);
            }
        }

        if(!(target instanceof EntityPlayer)){
            actor.addEvent(()-> {
                    Vec3d targetPos = target.getPositionVector();
                    actor.addEvent(() -> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                        EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                        sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                        actor.world.spawnEntity(sword);
                    }, 3);
            }, 1);


            actor.addEvent(()-> {
                    Vec3d targetPos = target.getPositionVector();
                    actor.addEvent(() -> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                        EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                        sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                        actor.world.spawnEntity(sword);
                    }, 3);
            }, 60);


            actor.addEvent(()-> {
                    Vec3d targetPos = target.getPositionVector();
                    actor.addEvent(() -> {
                        Vec3d targetedPos = target.getPositionVector();
                        Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetPos, targetedPos, 4);
                        EntityDauntlessSword sword = new EntityDauntlessSword(actor.world);
                        sword.setPosition(predictedPosition.x, target.posY + 10, predictedPosition.z);
                        actor.world.spawnEntity(sword);
                    }, 3);
            }, 120);
        }
    }
}
