package com.dungeon_additions.da.entity.sky_dungeon.high_king.king.action;

import com.dungeon_additions.da.entity.flame_knight.misc.EntityMoveTile;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.king.EntityHighKing;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ActionKingStomp implements IActionKing{

    private boolean isApathyr;

    public ActionKingStomp() {

    }

    public ActionKingStomp(boolean isApathyr) {
        this.isApathyr = isApathyr;
    }

    @Override
    public void performAction(EntityHighKing actor, EntityLivingBase target) {
        Vec3d savedPos = actor.getPositionVector();
        //Now we need to make this in a loop relative
        for(int t = 1; t < 10; t++ ) {
            ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    } else {
                        if(this.isApathyr) {
                            tile.setBlock(ModBlocks.DARK_GLOW_BRICKS, 0);
                        } else {
                            tile.setBlock(ModBlocks.CITY_BRICK_ALT, 0);
                        }
                    }
                    actor.world.spawnEntity(tile);

                });
        }

        actor.addEvent(()-> {
            for(int t = 10; t < 19; t++ ) {
                ModUtils.circleCallback(t, (4 * t), (pos) -> {
                    pos = new Vec3d(pos.x, 0, pos.y).add(savedPos);
                    EntityMoveTile tile = new EntityMoveTile(actor.world, actor, (float) (actor.getAttack() * 0.75));
                    tile.setPosition(pos.x, pos.y, pos.z);
                    int y = getSurfaceHeight(actor.world, new BlockPos(pos.x, 0, pos.z), (int) actor.posY - 6, (int) actor.posY + 4);
                    BlockPos posToo = new BlockPos(pos.x, y, pos.z);
                    tile.setOrigin(posToo, 5, posToo.getX() + 0.5D, posToo.getZ() + 0.5D);
                    tile.setLocationAndAngles(posToo.getX() + 0.5D, posToo.getY(), posToo.getZ() + 0.5D, 0.0f, 0.0F);
                    Block blockToo = actor.world.getBlockState(posToo).getBlock();
                    if(actor.world.getBlockState(posToo).isFullBlock()) {
                        tile.setBlock(blockToo, 0);
                    } else {
                        if(this.isApathyr) {
                            tile.setBlock(ModBlocks.DARK_GLOW_BRICKS, 0);
                        } else {
                            tile.setBlock(ModBlocks.CITY_BRICK_ALT, 0);
                        }
                    }
                    actor.world.spawnEntity(tile);

                });
            }
        }, 20);
    }

    private int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }
}
