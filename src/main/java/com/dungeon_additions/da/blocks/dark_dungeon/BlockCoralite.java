package com.dungeon_additions.da.blocks.dark_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCoralite extends BlockBase {
    public BlockCoralite(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(28) == 0 && worldIn.isAirBlock(pos.up())) {
            Vec3d particlePos = new Vec3d(pos.getX() + 0.5 + ModRand.getFloat(0.5F), pos.getY() + 1.2 + ModRand.getFloat(0.5F), pos.getZ() + 0.5 + ModRand.getFloat(0.5F));
            Main.proxy.spawnParticle(23, worldIn, particlePos.x, particlePos.y, particlePos.z, 0, 0.02, 0, 2039841);
        }


    }
}
