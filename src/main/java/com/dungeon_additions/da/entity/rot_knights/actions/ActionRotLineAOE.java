package com.dungeon_additions.da.entity.rot_knights.actions;

import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.ai.IAction;
import com.dungeon_additions.da.entity.rot_knights.EntityRotSpike;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionRotLineAOE implements IAction {
    @Override
    public void performAction(EntityAbstractBase actor, EntityLivingBase target) {
        if(actor.getHorizontalFacing() == EnumFacing.EAST || actor.getHorizontalFacing() == EnumFacing.WEST) {
            int maxDistance = 10;
            Vec3d startPosCenter = new Vec3d(actor.posX, actor.posY, actor.posZ);
            Vec3d startPosL1 = new Vec3d(actor.posX, actor.posY, actor.posZ + 1);
            Vec3d startPosL2 = new Vec3d(actor.posX , actor.posY, actor.posZ -1);
            Vec3d targetedPosCenter = new Vec3d(target.posX, startPosCenter.y, target.posZ);
            Vec3d targetedPosL1 = new Vec3d(target.posX, startPosCenter.y, target.posZ + 2);
            Vec3d targetedPosL2 = new Vec3d(target.posX, startPosCenter.y, target.posZ - 2);
            Vec3d dirCenter = targetedPosCenter.subtract(startPosCenter).normalize();
            Vec3d dirL1 = targetedPosL1.subtract(startPosL1).normalize();
            Vec3d dirL2 = targetedPosL2.subtract(startPosL2).normalize();

            ModUtils.lineCallback(startPosCenter.add(dirCenter), startPosCenter.add(dirCenter.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });

            ModUtils.lineCallback(startPosL1.add(dirL1), startPosL1.add(dirL1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });

            ModUtils.lineCallback(startPosL2.add(dirL2), startPosL2.add(dirL2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });
        } else if (actor.getHorizontalFacing() == EnumFacing.NORTH || actor.getHorizontalFacing() == EnumFacing.SOUTH) {
            int maxDistance = 10;

            Vec3d startPosCenter = new Vec3d(actor.posX, actor.posY, actor.posZ);
            Vec3d startPosL1 = new Vec3d(actor.posX + 1, actor.posY, actor.posZ);
            Vec3d startPosL2 = new Vec3d(actor.posX -1 , actor.posY, actor.posZ);
            Vec3d targetedPosCenter = new Vec3d(target.posX, startPosCenter.y, target.posZ);
            Vec3d targetedPosL1 = new Vec3d(target.posX + 2, startPosCenter.y, target.posZ);
            Vec3d targetedPosL2 = new Vec3d(target.posX - 2, startPosCenter.y, target.posZ);
            Vec3d dirCenter = targetedPosCenter.subtract(startPosCenter).normalize();
            Vec3d dirL1 = targetedPosL1.subtract(startPosL1).normalize();
            Vec3d dirL2 = targetedPosL2.subtract(startPosL2).normalize();

            ModUtils.lineCallback(startPosCenter.add(dirCenter), startPosCenter.add(dirCenter.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });

            ModUtils.lineCallback(startPosL1.add(dirL1), startPosL1.add(dirL1.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });

            ModUtils.lineCallback(startPosL2.add(dirL2), startPosL2.add(dirL2.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                EntityRotSpike arena = new EntityRotSpike(actor.world);
                int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 3, (int) actor.posY + 2);
                if(y != 0) {
                    arena.setPosition(pos.x, y + 1, pos.z);
                    actor.world.spawnEntity(arena);
                }
            });
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
