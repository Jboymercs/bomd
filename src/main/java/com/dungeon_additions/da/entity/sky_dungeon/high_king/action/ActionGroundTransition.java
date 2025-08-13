package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.night_lich.EntityLichStaffAOE;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionGroundTransition implements IActionDrake{

    public final int lengthOfAOE;

    public ActionGroundTransition(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }
    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        //this keeps the origin of the wave to this point even if the boss moves after the animation ends
        Vec3d savedPos = actor.getPositionVector();

        if(lengthOfAOE > 10) {
            for(int t = 1; t < 10; t++ ) {

                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityDragonAOE tile = new EntityDragonAOE(actor.world);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 6);
                    BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);
                });
                actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.0f, 1.0F);
            }

        } else {
            for(int t = 1; t < lengthOfAOE; t++ ) {

                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityDragonAOE tile = new EntityDragonAOE(actor.world);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                    BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);
                });
                actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.0f, 1.0F);
            }
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
