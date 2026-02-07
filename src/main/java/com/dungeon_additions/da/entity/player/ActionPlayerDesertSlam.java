package com.dungeon_additions.da.entity.player;

import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionPlayerDesertSlam implements IActionPlayer {

    private int lengthOfAOE;

    public ActionPlayerDesertSlam(int lengthofAOE) {
        this.lengthOfAOE = lengthofAOE + 4;
    }
    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d playerPos = actor.getPositionVector();
        float damage = 6 + ModUtils.addAbilityBonusDamage(actor.getHeldItemMainhand(), 1.25F);

        for(int i = 0; i < lengthOfAOE; i++) {
            int finalI = i;
            ModUtils.circleCallback(finalI, 4 * finalI, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(playerPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) playerPos.y - 5, (int) playerPos.y + 2);
                EntityMoveTile spike = new EntityMoveTile(actor.world, actor, damage);
                spike.setPosition(pos.x, y + 1, pos.z);
                BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                spike.setOrigin(posToo, 5 + (finalI * 2), posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                spike.setLocationAndAngles(posToo.getX() + 0.5D, y, posToo.getZ() + 0.5D, 0.0f, 0.0F);
                spike.setBlock(Blocks.SAND, 0);
                actor.world.spawnEntity(spike);
            });
        }
    }

    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
