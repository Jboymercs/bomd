package com.dungeon_additions.da.entity.dark_dungeon.dauntless;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.gaelon_dungeon.EntityApathyrSpear;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionDauntlessSelfAOE implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();

        ModUtils.circleCallback(1, 4, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
            EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1 );
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });

        actor.addEvent(()-> {
            ModUtils.circleCallback(2, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 3);

        actor.addEvent(()-> {
            ModUtils.circleCallback(3, 12, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1 );
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 6);

        actor.addEvent(()-> {
            ModUtils.circleCallback(4, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 9);

        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 12);

        actor.addEvent(()-> {
            ModUtils.circleCallback(6, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 15);

        actor.addEvent(()-> {
            ModUtils.circleCallback(7, 28, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 18);

        actor.addEvent(()-> {
            ModUtils.circleCallback(8, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 21);

        actor.addEvent(()-> {
            ModUtils.circleCallback(9, 36, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 24);

        actor.addEvent(()-> {
            ModUtils.circleCallback(10, 40, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 27);

        actor.addEvent(()-> {
            ModUtils.circleCallback(11, 32, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 30);

        actor.addEvent(()-> {
            ModUtils.circleCallback(12, 24, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 33);

        actor.addEvent(()-> {
            ModUtils.circleCallback(13, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 36);

        actor.addEvent(()-> {
            ModUtils.circleCallback(14, 8, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 12, (int) actor.posY + 2);
                EntityDauntlessAOE spike = new EntityDauntlessAOE(actor.world, 15 ,1);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
        }, 39);

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
