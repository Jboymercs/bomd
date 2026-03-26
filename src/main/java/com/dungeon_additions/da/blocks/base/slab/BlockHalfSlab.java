package com.dungeon_additions.da.blocks.base.slab;

import com.dungeon_additions.da.init.ModItems;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockHalfSlab extends BlockSlabBase{
    public BlockHalfSlab(String name, Material maeterialIn, CreativeTabs tabs, BlockSlab half, BlockSlab doubleSlab, float hardness, float resistance, SoundType soundType) {

        super(name, maeterialIn, half);
        this.setCreativeTab(tabs);
        ModItems.ITEMS.add(new ItemSlab(this, this, doubleSlab).setRegistryName(name));
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);

    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {

        return new ItemStack(this);
    }

    @Override
    public boolean isDouble() {
        return false;
    }
}
