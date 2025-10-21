package com.dungeon_additions.da.blocks;
import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockGaelonCrystalBlock  extends BlockBase {


    public BlockGaelonCrystalBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.fullBlock = true;
        this.needsRandomTick = true;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(ModItems.GAELON_SHARD, 1));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(29) == 0) {

            Main.proxy.spawnParticle(15, worldIn, pos.getX() + rand.nextInt(4), pos.getY() + rand.nextInt(2), pos.getZ() + rand.nextInt(4), worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 0.04, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 100);
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.SOLID;
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) return;
        if (world.rand.nextFloat() > 0.2) return;
        EnumFacing facing = EnumFacing.values()[random.nextInt(6)];
        BlockPos pos1 = pos.offset(facing);
        IBlockState state1 = world.getBlockState(pos1);
        if (state1.getMaterial() == Material.AIR) world.setBlockState(pos1, ModBlocks.GAELON_CHUNK
                .getDefaultState().withProperty(BlockDirectional.FACING, facing), 3);
        if (state1.getBlock() instanceof BlockGaelonCrystal) {
            return;
        }
    }
}
