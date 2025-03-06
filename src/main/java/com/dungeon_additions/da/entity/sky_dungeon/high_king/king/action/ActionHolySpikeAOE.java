package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyAOE;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionHolySpikeAOE implements IActionKing{

    private final int lengthOfAOE;

    public ActionHolySpikeAOE(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        if(lengthOfAOE >= 14) {
            for(int t = 1; t < 14; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        actor.world.spawnEntity(tile);

                    });
                }, t);
            }
        } else {
            for(int t = 1; t < lengthOfAOE; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityKingHolyAOE tile = new EntityKingHolyAOE(actor.world);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        actor.world.spawnEntity(tile);

                    });
                }, t);
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
