package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.blossom.EntityVoidSpike;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class ActionMegaStompAOE implements IAction {

    private boolean doesLine;

    private double distance;
    public ActionMegaStompAOE(boolean doesLine, double distance) {
        this.doesLine = doesLine;
        this.distance = distance;
    }
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d savedPos = target.getPositionVector();
        actor.addEvent(()-> {
            if(doesLine) {
                this.createLineTooTarget(actor, savedPos, distance + 3);
            }
        }, 1);

        ModUtils.circleCallback(3, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 3);
            EntityIcicleSpike spike = new EntityIcicleSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(4, 24, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 3);
            EntityIcicleSpike spike = new EntityIcicleSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(5, 28, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 3);
            EntityIcicleSpike spike = new EntityIcicleSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(6, 32, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 3);
            EntityIcicleSpike spike = new EntityIcicleSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
        ModUtils.circleCallback(7, 36, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(actor.getPositionVector());
            int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 4, (int) actor.posY + 3);
            EntityIcicleSpike spike = new EntityIcicleSpike(actor.world);
            spike.setPosition(pos.x, y + 1, pos.z);
            actor.world.spawnEntity(spike);
        });
    }

    private void createLineTooTarget(EntityAbstractBase actor, Vec3d savedPos, double distance) {
        Vec3d targetPos = savedPos;
        Vec3d currentPos = actor.getPositionVector();
        Vec3d dir = targetPos.subtract(currentPos).normalize();
        AtomicReference<Vec3d> spawnPos = new AtomicReference<>(currentPos);

        for (int t = 0; t < distance; t += 1) {
            int additive = t;
            actor.addEvent(() -> {

                ModUtils.lineCallback(currentPos.add(dir), currentPos.add(dir.scale(additive)), additive * 2, (pos, r) -> {
                    spawnPos.set(pos);
                });
                Vec3d initPos = spawnPos.get();
                int y = getSurfaceHeight(actor.world, new BlockPos(initPos.x, 0, initPos.z), (int) actor.posY - 4, (int) actor.posY + 3);
                EntityIcicleSpike crystal = new EntityIcicleSpike(actor.world);

                crystal.setPosition(initPos.x, y + 1, initPos.z);
                actor.world.spawnEntity(crystal);

            }, t);
        }
        Vec3d targtetPos2 = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d currPos2 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d dir2 = targtetPos2.subtract(currPos2).normalize();
        AtomicReference<Vec3d> spawnPos2 = new AtomicReference<>(currPos2);

        for (int t = 0; t < distance; t += 1) {
            int additive = t;
            actor.addEvent(() -> {

                ModUtils.lineCallback(currPos2.add(dir2), currPos2.add(dir2.scale(additive)), additive * 2, (pos, r) -> {
                    spawnPos2.set(pos);
                });
                Vec3d initPos = spawnPos2.get();
                int y = getSurfaceHeight(actor.world, new BlockPos(initPos.x, 0, initPos.z), (int) actor.posY - 4, (int) actor.posY + 3);
                EntityIcicleSpike crystal = new EntityIcicleSpike(actor.world);

                crystal.setPosition(initPos.x, y + 1, initPos.z);
                actor.world.spawnEntity(crystal);

            }, t);
        }

        Vec3d targtetPos3 = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d currPos3 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d dir3 = targtetPos3.subtract(currPos3).normalize();
        AtomicReference<Vec3d> spawnPos3 = new AtomicReference<>(currPos3);

        for (int t = 0; t < distance; t += 1) {
            int additive = t;
            actor.addEvent(() -> {

                ModUtils.lineCallback(currPos3.add(dir3), currPos3.add(dir3.scale(additive)), additive * 2, (pos, r) -> {
                    spawnPos3.set(pos);
                });
                Vec3d initPos = spawnPos3.get();
                int y = getSurfaceHeight(actor.world, new BlockPos(initPos.x, 0, initPos.z), (int) actor.posY - 4, (int) actor.posY + 3);
                EntityIcicleSpike crystal = new EntityIcicleSpike(actor.world);

                crystal.setPosition(initPos.x, y + 1, initPos.z);
                actor.world.spawnEntity(crystal);

            }, t);
        }

        Vec3d targtetPos4 = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 2)));
        Vec3d currPos4 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 2)));
        Vec3d dir4 = targtetPos4.subtract(currPos4).normalize();
        AtomicReference<Vec3d> spawnPos4 = new AtomicReference<>(currPos4);

        for (int t = 0; t < distance; t += 1) {
            int additive = t;
            actor.addEvent(() -> {

                ModUtils.lineCallback(currPos4.add(dir4), currPos4.add(dir4.scale(additive)), additive * 2, (pos, r) -> {
                    spawnPos4.set(pos);
                });
                Vec3d initPos = spawnPos4.get();
                int y = getSurfaceHeight(actor.world, new BlockPos(initPos.x, 0, initPos.z), (int) actor.posY - 4, (int) actor.posY + 3);
                EntityIcicleSpike crystal = new EntityIcicleSpike(actor.world);

                crystal.setPosition(initPos.x, y + 1, initPos.z);
                actor.world.spawnEntity(crystal);

            }, t);
        }

        Vec3d targtetPos5 = targetPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -2)));
        Vec3d currPos5 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -2)));
        Vec3d dir5 = targtetPos5.subtract(currPos5).normalize();
        AtomicReference<Vec3d> spawnPos5 = new AtomicReference<>(currPos5);

        for (int t = 0; t < distance; t += 1) {
            int additive = t;
            actor.addEvent(() -> {

                ModUtils.lineCallback(currPos5.add(dir5), currPos5.add(dir5.scale(additive)), additive * 2, (pos, r) -> {
                    spawnPos5.set(pos);
                });
                Vec3d initPos = spawnPos5.get();
                int y = getSurfaceHeight(actor.world, new BlockPos(initPos.x, 0, initPos.z), (int) actor.posY - 4, (int) actor.posY + 3);
                EntityIcicleSpike crystal = new EntityIcicleSpike(actor.world);

                crystal.setPosition(initPos.x, y + 1, initPos.z);
                actor.world.spawnEntity(crystal);

            }, t);
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
