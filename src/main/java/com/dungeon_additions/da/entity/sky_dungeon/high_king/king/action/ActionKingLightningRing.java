package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionKingLightningRing implements IActionKing{
    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();
        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 14, (pos)-> {
                int i = 0;
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z));
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
                i += 2;
            });
        }, 16);
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
