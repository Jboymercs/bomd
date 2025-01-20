package com.dungeon_additions.da.entity.frost_dungeon.great_wyrk;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.frost_dungeon.EntityAbstractGreatWyrk;
import com.dungeon_additions.da.entity.frost_dungeon.EntityIcicleSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicReference;

public class ActionStarStomp implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d actorPos1 = actor.getPositionVector().add(3, 0, 0);
        Vec3d actorPos2 = actor.getPositionVector().add(-3, 0, 0);
        Vec3d actorPos3 = actor.getPositionVector().add(0, 0, 3);
        Vec3d actorPos4 = actor.getPositionVector().add(0, 0, -3);
        Vec3d actorPos5 = actor.getPositionVector().add(4, 0, 4);
        Vec3d actorPos6 = actor.getPositionVector().add(4, 0, -4);
        Vec3d actorPos7 = actor.getPositionVector().add(-4, 0, 4);
        Vec3d actorPos8 = actor.getPositionVector().add(-4, 0, -4);


        Vec3d targetPos1 = actor.getPositionVector().add(23, 0, 0);
        Vec3d targetPos2 = actor.getPositionVector().add(-23, 0, 0);
        Vec3d targetPos3 = actor.getPositionVector().add(0, 0, 23);
        Vec3d targetPos4 = actor.getPositionVector().add(0, 0, -23);
        Vec3d targetPos5 = actor.getPositionVector().add(24, 0, 24);
        Vec3d targetPos6 = actor.getPositionVector().add(24, 0, -24);
        Vec3d targetPos7 = actor.getPositionVector().add(-24, 0, 24);
        Vec3d targetPos8 = actor.getPositionVector().add(-24, 0, -24);

        summonDirectionalSpikeLine(actor, actorPos1, targetPos1);
        summonDirectionalSpikeLine(actor, actorPos2, targetPos2);
        summonDirectionalSpikeLineCross(actor, actorPos3, targetPos3);
        summonDirectionalSpikeLineCross(actor, actorPos4, targetPos4);

        actor.addEvent(()-> {
            summonDirectionalSpikeLine(actor, actorPos5, targetPos5);
            summonDirectionalSpikeLine(actor, actorPos6, targetPos6);
            summonDirectionalSpikeLine(actor, actorPos7, targetPos7);
            summonDirectionalSpikeLine(actor, actorPos8, targetPos8);
        }, 20);

        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        for(int t = 3; t < 8; t++ ) {
            int finalT = t;
            actor.addEvent(()-> {
                ModUtils.circleCallback(finalT, (5 * finalT), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityIcicleSpike tile = new EntityIcicleSpike(actor.world);
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 3);
                    BlockPos posToo = new BlockPos(pos.x, y + 1, pos.z);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    actor.world.spawnEntity(tile);

                });
            }, 2 * t);
        }
    }

    private void summonDirectionalSpikeLineCross(EntityAbstractBase actor, Vec3d startPos, Vec3d dirPos) {
        Vec3d targetPos1 = dirPos;
        Vec3d currentPos = startPos;
        Vec3d dir = targetPos1.subtract(currentPos).normalize();
        AtomicReference<Vec3d> spawnPos = new AtomicReference<>(currentPos);

        for (int t = 0; t < 20; t += 1) {
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

        Vec3d targtetPos2 = targetPos1.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
        Vec3d currPos2 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(1, 0, 0)));
        Vec3d dir2 = targtetPos2.subtract(currPos2).normalize();
        AtomicReference<Vec3d> spawnPos2 = new AtomicReference<>(currPos2);

        for (int t = 0; t < 20; t += 1) {
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

        Vec3d targtetPos3 = targetPos1.add(ModUtils.getRelativeOffset(actor, new Vec3d(-1, 0, 0)));
        Vec3d currPos3 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(-1, 0, 0)));
        Vec3d dir3 = targtetPos3.subtract(currPos3).normalize();
        AtomicReference<Vec3d> spawnPos3 = new AtomicReference<>(currPos3);

        for (int t = 0; t < 20; t += 1) {
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
    }

    private void summonDirectionalSpikeLine(EntityAbstractBase actor,Vec3d stargPos, Vec3d dirPos) {
        Vec3d targetPos1 = dirPos;
        Vec3d currentPos = stargPos;
        Vec3d dir = targetPos1.subtract(currentPos).normalize();
        AtomicReference<Vec3d> spawnPos = new AtomicReference<>(currentPos);

        for (int t = 0; t < 20; t += 1) {
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

        Vec3d targtetPos2 = targetPos1.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d currPos2 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, 1)));
        Vec3d dir2 = targtetPos2.subtract(currPos2).normalize();
        AtomicReference<Vec3d> spawnPos2 = new AtomicReference<>(currPos2);

        for (int t = 0; t < 20; t += 1) {
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

        Vec3d targtetPos3 = targetPos1.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d currPos3 = currentPos.add(ModUtils.getRelativeOffset(actor, new Vec3d(0, 0, -1)));
        Vec3d dir3 = targtetPos3.subtract(currPos3).normalize();
        AtomicReference<Vec3d> spawnPos3 = new AtomicReference<>(currPos3);

        for (int t = 0; t < 20; t += 1) {
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
