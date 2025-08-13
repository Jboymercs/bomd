package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king_projectiles.EntityDragonAOE;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ActionKingCastQuickAOE implements IActionDrake{
    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        Vec3d targetPos = target.getPositionVector();
            ModUtils.circleCallback(0, 1, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(1, 4, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(2, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.9f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            actor.addEvent(()-> {
                ModUtils.circleCallback(3, 12, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                    EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(spike);
                });

                ModUtils.circleCallback(4, 16, (pos)-> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                    EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                    spike.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(spike);
                });
                actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.9f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
            }, 20);
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
