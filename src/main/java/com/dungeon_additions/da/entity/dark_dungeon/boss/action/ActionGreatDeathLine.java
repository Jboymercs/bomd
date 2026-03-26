package com.dungeon_additions.da.entity.dark_dungeon.boss.action;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.dark_dungeon.boss.EntityGreatDeath;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionGreatDeathLine implements IAction {

    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        Vec3d center = actor.getPositionVector();
        Vec3d plotPointX = center.add(18, 0, 18);
        Vec3d plotPointX2 = center.add(15, 0, 15);
        Vec3d plotPointX3 = center.add(18, 0, 12);
        Vec3d plotPointX4 = center.add(15, 0, 9);
        Vec3d plotPointX5 = center.add(18, 0, 6);
        Vec3d plotPointX6 = center.add(15, 0, 3);
        Vec3d plotPointCenter = center.add(18, 0, 0);
        Vec3d plotPointX7 = center.add(15, 0, -3);
        Vec3d plotPointX8 = center.add(18, 0, -6);
        Vec3d plotPointX9 = center.add(15, 0, -9);
        Vec3d plotPointX10 = center.add(18, 0, -12);
        Vec3d plotPointX11 = center.add(15, 0, -15);
        Vec3d plotPointX12 = center.add(18, 0, -18);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX2.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX3.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX4.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX5.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX6.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        //center
        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointCenter.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX7.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX8.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX9.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX10.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
        }

        actor.addEvent(()-> {
            for(int i = 0; i <= 36; i += 3) {
                EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
                Vec3d relPos5 = plotPointX11.add(-i, 0, 0);
                int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
                bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
                actor.world.spawnEntity(bolt5);
            }
        }, 40);

        for(int i = 0; i <= 36; i += 3) {
            EntityGreatDeath bolt5 = new EntityGreatDeath(actor.world);
            Vec3d relPos5 = plotPointX12.add(-i, 0, 0);
            int yVar5 = getSurfaceHeight(actor.world, new BlockPos(relPos5.x, relPos5.y, relPos5.z), (int) actor.posY - 3, (int) actor.posY + 4);
            bolt5.setPosition(relPos5.x, relPos5.y, relPos5.z);
            actor.world.spawnEntity(bolt5);
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

        return pos.getY() - 1;
    }
}
