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

public class ActionDragonStomp implements IActionDrake{

    private Vec3d orginPos;

    public ActionDragonStomp(Vec3d ori) {
        orginPos = ori;
    }

    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        ModUtils.circleCallback(1, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityDragonAOE spike = new EntityDragonAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        ModUtils.circleCallback(2, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
            EntityDragonAOE spike = new EntityDragonAOE(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);

        actor.addEvent(()-> {
            ModUtils.circleCallback(3, 12, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(4, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);
        }, 5);

        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });

            ModUtils.circleCallback(6, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);
        }, 10);

        actor.addEvent(()-> {
            ModUtils.circleCallback(7, 28, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);
        }, 15);

        actor.addEvent(()-> {
            ModUtils.circleCallback(8, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);
        }, 15);

        actor.addEvent(()-> {
            ModUtils.circleCallback(9, 40, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(orginPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 5);
                EntityDragonAOE spike = new EntityDragonAOE(actor.world);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.HIGH_DRAKE_ROCK_AOE, 1.5f, 1.0F);
        }, 15);
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
