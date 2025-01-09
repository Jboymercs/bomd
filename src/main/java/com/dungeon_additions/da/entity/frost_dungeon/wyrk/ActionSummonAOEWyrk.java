package com.dungeon_additions.da.entity.frost_dungeon.wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionSummonAOEWyrk implements IAction {

    private final int lengthOfAOE;

    public ActionSummonAOEWyrk(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        //this keeps the origin of the wave to this point even if the boss moves after the animation ends
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        for(int t = 1; t < lengthOfAOE; t++ ) {
            int finalT = t;
            actor.addEvent(()-> {
                ModUtils.circleCallback(finalT, (6 * finalT), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY() + 1, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);

                });
            }, 3 * t);
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
