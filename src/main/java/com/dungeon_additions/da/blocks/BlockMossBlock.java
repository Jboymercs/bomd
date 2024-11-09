package com.dungeon_additions.da.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class BlockMossBlock extends BlockBase
{
    public BlockMossBlock(String name, Material material, float hardness, float resistance, SoundType soundType)
    { super(name, material, hardness, resistance, soundType); }

    /** Allow anything but a Cactus */
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        if (plantable.getPlantType(world, pos) == EnumPlantType.Plains) return true;
        /* We love Dead Bushes in this household */
        if (plantable.getPlant(world, pos).getBlock() == Blocks.DEADBUSH) return true;
        /* Beach-type plants like Sugar Cane have a hardcoded check, so we need to implement our own water check */
        if (plantable.getPlantType(world, pos) == EnumPlantType.Beach)
        {
            for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
            { if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER) return true; }
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }
}