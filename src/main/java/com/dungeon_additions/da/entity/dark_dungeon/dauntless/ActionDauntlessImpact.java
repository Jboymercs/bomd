package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDauntless;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.generic.EntityBlastTile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ActionDauntlessImpact implements IAction {

    private final int lengthOfAOE;
    private Vec3d center = null;
    private int reduceChance = 0;
    public ActionDauntlessImpact(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }

    public ActionDauntlessImpact(Vec3d center, int lengthOfAOE, int reduceChance) {
        this.lengthOfAOE = lengthOfAOE;
        this.center = center;
        this.reduceChance = reduceChance;
    }

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        //this keeps the origin of the wave to this point even if the boss moves after the animation ends
        Vec3d savedPos = actor.getPositionVector();
        if(center != null) {
            savedPos = center;
        }
        //Now we need to make this in a loop relative
        for(int t = 1; t < lengthOfAOE; t++ ) {
            int finalT = t;
            Vec3d finalSavedPos = savedPos;
            actor.addEvent(()-> {
                ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(finalSavedPos);
                    Random rand = new Random();
                    EntityBlastTile tile = new EntityBlastTile(actor.world, actor, reduceChance != 0 && rand.nextInt(reduceChance) == 0);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) finalSavedPos.y - 6, (int) finalSavedPos.y + 4);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOriginCenter(posToo, 1, posToo.getX() + 0.5D, posToo.getZ() + 0.5D, finalSavedPos);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    } else {
                        tile.setBlock(Blocks.STONE, 0);
                    }
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
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

}
