package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityKingHolyAOE;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionRotProgressive implements IAction {

    private final int lengthOfAOE;

    public ActionRotProgressive(int lengthOfAOE) {
        this.lengthOfAOE = lengthOfAOE;
    }


    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        if(lengthOfAOE >= 17) {
            for(int t = 1; t < 17; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityRotSpike tile = new EntityRotSpike(actor.world);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) pos.y - 3, (int) pos.y + 5);
                        tile.setPosition(pos.x, y + 1, pos.z);
                        actor.world.spawnEntity(tile);

                    });
                }, t * 4);
            }
        } else {
            for(int t = 1; t < lengthOfAOE; t++ ) {
                int finalT = t;
                actor.addEvent(()-> {
                    ModUtils.circleCallback(finalT, (4 * finalT), (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                        EntityRotSpike tile = new EntityRotSpike(actor.world);
                        int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) pos.y - 3, (int) pos.y + 5);
                        tile.setPosition(pos.x, y + 1, pos.z);
                        actor.world.spawnEntity(tile);

                    });
                }, t * 4);
            }
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
