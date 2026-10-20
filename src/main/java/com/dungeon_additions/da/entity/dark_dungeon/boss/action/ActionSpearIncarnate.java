package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntitySpearIncarnate;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ActionSpearIncarnate implements IAction {

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {

        if(target instanceof EntityPlayer) {
            List<EntityPlayer> nearbyPlayers = actor.world.getEntitiesWithinAABB(EntityPlayer.class, actor.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty()) {
                for(EntityPlayer player : nearbyPlayers) {
                    if(!player.isCreative() && !player.isSpectator()) {
                        actor.addEvent(()-> {
                            Vec3d targetOldPos = player.getPositionVector();
                            actor.addEvent(()-> {
                                Vec3d targetedPos = player.getPositionVector();
                                Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                                EntitySpearIncarnate spearIncarnate = new EntitySpearIncarnate(actor.world, actor.getAttack() * 0.5F);
                                int y = getSurfaceHeight(actor.world, new BlockPos(predictedPosition.x, 0, predictedPosition.z), (int) player.posY - 5, (int) player.posY + 2);
                                spearIncarnate.setPosition(predictedPosition.x, y + 1, predictedPosition.z);
                                actor.world.spawnEntity(spearIncarnate);
                            }, 3);
                        }, 1);
                    }
                }
            }
        } else {
            actor.addEvent(()-> {
                Vec3d targetOldPos = target.getPositionVector();
                actor.addEvent(()-> {
                    Vec3d targetedPos = target.getPositionVector();
                    Vec3d predictedPosition = ModUtils.predictPlayerPosition(targetOldPos, targetedPos, 3);
                    EntitySpearIncarnate spearIncarnate = new EntitySpearIncarnate(actor.world, actor.getAttack() * 0.5F);
                    int y = getSurfaceHeight(actor.world, new BlockPos(target.posX, 0, target.posZ), (int) targetedPos.y - 5, (int) targetedPos.y + 7);
                    spearIncarnate.setPosition(predictedPosition.x, y + 1, predictedPosition.z);
                    actor.world.spawnEntity(spearIncarnate);
                }, 3);
            }, 1);
        }
    }


    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
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
