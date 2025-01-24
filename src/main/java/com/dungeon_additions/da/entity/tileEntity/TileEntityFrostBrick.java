package com.dungeon_additions.da.entity.tileEntity;

import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityFrostBrick extends TileEntity implements ITickable {

    private int age = 0;
    @Override
    public void update() {
        if(world.isRemote) {
            return;
        }

        if(world.getBlockState(pos.add(0, -1, 0)).getBlock() == ModBlocks.ICICLE_BLOCK) {
            age = 0;
            return;
        }

        if(world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.ICE && world.isAirBlock(pos.add(0, -1, 0))) {
            if(world.rand.nextInt(300) == 0) {
                age ++;
            }

            if(age >= 10) {
                world.setBlockState(pos.add(0, -1, 0), ModBlocks.ICICLE_BLOCK.getDefaultState());
            }
        }
    }
}
