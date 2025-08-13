package com.dungeon_additions.da.entity.sky_dungeon.high_king.action;

import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ActionKingSelfAOE implements IActionDrake{


    @Override
    public void performAction(EntityHighKingDrake actor, EntityLivingBase target) {
        Vec3d targetPos = actor.getPositionVector();

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
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.4f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 8);

        actor.addEvent(()-> {
            ModUtils.circleCallback(4, 16, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.5f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 16);

        actor.addEvent(()-> {
            ModUtils.circleCallback(5, 20, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
                if(actor.world.rand.nextInt(13) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.6f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 24);

        actor.addEvent(()-> {
            ModUtils.circleCallback(6, 36, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                actor.world.spawnEntity(spike);
                if(actor.world.rand.nextInt(15) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.7f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 32);

        actor.addEvent(()-> {
            ModUtils.circleCallback(7, 42, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(17) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.7f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 40);

        actor.addEvent(()-> {
            ModUtils.circleCallback(8, 56, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(20) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.7f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 48);

        actor.addEvent(()-> {
            ModUtils.circleCallback(9, 64, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(23) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.6f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 56);

        actor.addEvent(()-> {
            ModUtils.circleCallback(10, 68, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(27) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.7f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 62);

        actor.addEvent(()-> {
            ModUtils.circleCallback(11, 72, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(30) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.7f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 70);

        actor.addEvent(()-> {
            ModUtils.circleCallback(12, 78, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(33) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.6f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 78);

        actor.addEvent(()-> {
            ModUtils.circleCallback(14, 82, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(36) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.5f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 86);

        actor.addEvent(()-> {
            ModUtils.circleCallback(16, 86, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y).add(targetPos);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 9, (int) actor.posY + 5);
                EntitySkyBolt spike = new EntitySkyBolt(actor.world, new Vec3d(pos.x, y + 10, pos.z), true);
                spike.setPosition(pos.x, y + 1, pos.z);
                if(actor.world.rand.nextInt(38) == 0) {
                    actor.world.spawnEntity(spike);
                }
            });
            actor.playSound(SoundsHandler.SKY_LIGHTNING_CAST, 1.4f, 0.8f / (new Random().nextFloat() * 0.4f + 0.6f));
        }, 92);
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
