package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.blocks.desert_dungeon.BlockGrumDispenser;
import com.dungeon_additions.da.entity.frost_dungeon.ProjectileFrostBullet;
import com.dungeon_additions.da.entity.projectiles.puzzle.ProjectilePuzzleBall;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TileEntityGrumDispenser extends TileEntity implements ITickable {

    private int tickByte = 0;

    @Override
    public void update() {
        EnumFacing facing = BlockGrumDispenser.getFacing(this.getBlockMetadata());

        tickByte++;

        //spawns a projectile dependendant on facing
        if(tickByte > 120) {

            Vec3d vel = new Vec3d(facing.getDirectionVec()).scale(0.5);
            Vec3d vel2 = new Vec3d(facing.getDirectionVec()).scale(8);
            BlockPos basePos = this.getPos().add(0.5, 0.5, 0.5);

            ProjectilePuzzleBall bullet = new ProjectilePuzzleBall(world);
            if(facing.equals(EnumFacing.UP)) {
                bullet.setPosition(basePos.getX() + vel.x + 0.5, basePos.getY() + 1, basePos.getZ() + vel.z + 0.5);
            } else if (facing.equals(EnumFacing.DOWN)) {
                bullet.setPosition(basePos.getX() + vel.x + 0.5, basePos.getY() -0.75, basePos.getZ() + vel.z + 0.5);
            } else if (facing.equals(EnumFacing.EAST)) {
                bullet.setPosition(basePos.getX() + vel.x + 0.75, basePos.getY() + 0.5, basePos.getZ() + vel.z + 0.5);
            } else if (facing.equals(EnumFacing.WEST)) {
                bullet.setPosition(basePos.getX() + vel.x - 0.25, basePos.getY() + 0.5, basePos.getZ() + vel.z + 0.5);
            }  else if (facing.equals(EnumFacing.SOUTH)) {
                bullet.setPosition(basePos.getX() + vel.x + 0.5, basePos.getY() + 0.5, basePos.getZ() + vel.z + 0.75);
            }  else if (facing.equals(EnumFacing.NORTH)) {
                bullet.setPosition(basePos.getX() + vel.x + 0.5, basePos.getY() + 0.5, basePos.getZ() + vel.z - 0.25);
            } else {
                bullet.setPosition(basePos.getX() + vel.x, basePos.getY() + 0.5, basePos.getZ() + vel.z);
            }

            Vec3d vecBase = new Vec3d(basePos.getX(), basePos.getY(), basePos.getZ());

            bullet.setTravelRange(10);
            Vec3d targetPos = new Vec3d(basePos.getX() + vel2.x, basePos.getY() + vel2.y, basePos.getZ() + vel2.z);
            if(facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN)) {
                targetPos = new Vec3d(basePos.getX() + vel2.x + 0.5, basePos.getY() + vel2.y, basePos.getZ() + vel2.z + 0.5);
            } else if (facing.equals(EnumFacing.EAST)) {
                targetPos = new Vec3d(basePos.getX() + vel2.x + 0.5, basePos.getY() + vel2.y + 0.5, basePos.getZ() + vel2.z + 0.5);
            } else if (facing.equals(EnumFacing.WEST)) {
                targetPos = new Vec3d(basePos.getX() + vel2.x - 0.25, basePos.getY() + vel2.y + 0.5, basePos.getZ() + vel2.z + 0.5);
            } else if (facing.equals(EnumFacing.SOUTH)) {
                targetPos = new Vec3d(basePos.getX() + vel2.x + 0.5, basePos.getY() + vel2.y + 0.5, basePos.getZ() + vel2.z + 0.75);
            } else if (facing.equals(EnumFacing.NORTH)) {
                targetPos = new Vec3d(basePos.getX() + vel2.x + 0.5, basePos.getY() + vel2.y + 0.5 , basePos.getZ() + vel2.z - 0.25);
            }

            Vec3d fromTargetTooActor = vecBase.subtract(targetPos);

            Vec3d lineDir = ModUtils.rotateVector2(fromTargetTooActor.crossProduct(ModUtils.Y_AXIS), fromTargetTooActor, 0).normalize().scale(0);
            Vec3d lineStart = targetPos.subtract(lineDir);
            Vec3d lineEnd = targetPos.add(lineDir);
            if(!world.isRemote) {
                world.spawnEntity(bullet);
                bullet.setNoGravity(true);
                float speed = (float) 0.25;
                ModUtils.lineCallback(lineStart, lineEnd, 1, (posb, i) -> {
                    ModUtils.throwProjectileNoSpawn(posb,bullet,1F, speed);
                });
            }

            if(world.isRemote) {
                world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.MIRROR_DISPENSE, SoundCategory.BLOCKS, 0.6F, world.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            tickByte = 0;
        }
    }
}
