package com.dungeon_additions.da.entity.player;
import com.dungeon_additions.da.entity.void_dungeon.EntityVoidclysmSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionSummonVoidSpikes implements IActionPlayer{
    private float damageIn;

    public ActionSummonVoidSpikes(float damageIn) {
        this.damageIn = damageIn;
    }

    @Override
    public void performAction(EntityPlayer actor) {
        Vec3d targetPos = actor.getPositionVector();

        ModUtils.circleCallback(1, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(2, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(3, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(4, 12, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(5, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(6, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 1);
            EntityVoidclysmSpike spike = new EntityVoidclysmSpike(actor.world, actor, damageIn);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

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
