package com.dungeon_additions.da.entity.frost_dungeon.draugr.draugr_elite;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionDraugrStomp implements IAction {
    private final int lengthOfAOE;

    public ActionDraugrStomp(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        //this keeps the origin of the wave to this point even if the boss moves after the animation ends
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        if(lengthOfAOE >= 16) {
            for(int t = 1; t < 16; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                        tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                        tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                        tile.setBlock(Blocks.PACKED_ICE, 0);
                        actor.world.spawnEntity(tile);

                    });
                }, 3 * t);
            }
        } else {
            for(int t = 1; t < lengthOfAOE; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityMoveTile tile = new EntityMoveTile(actor.world, actor);
                        tile.setPosition(pos.x, pos.y, pos.z);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                        BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                        tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                        tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                        tile.setBlock(Blocks.PACKED_ICE, 0);
                        actor.world.spawnEntity(tile);

                    });
                }, 3 * t);
            }
        }
    }


    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.isBlockFullCube(pos.add(0, currentY, 0))) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
