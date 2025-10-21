package com.dungeon_additions.da.blocks;
import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockGaelonCrystal extends BlockBase{

    public static final PropertyDirection FACING = BlockDirectional.FACING;
    protected static final AxisAlignedBB CRYSTAL_SIZE = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 1D, 1D);

    public BlockGaelonCrystal(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 6]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(29) == 0) {

            Main.proxy.spawnParticle(15, worldIn, pos.getX() + rand.nextInt(4), pos.getY() + rand.nextInt(2), pos.getZ() + rand.nextInt(4), worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 0.04, worldIn.rand.nextFloat()/6 - worldIn.rand.nextFloat()/6, 100);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_SIZE;
    }

    private static AxisAlignedBB createAABB(EnumFacing facing, double distanceFromEdge, double height) {
        switch (facing) {
            case DOWN:
                return new AxisAlignedBB(distanceFromEdge, 1 - height, distanceFromEdge,
                        1 - distanceFromEdge, 1, 1 - distanceFromEdge);
            case NORTH:
                return new AxisAlignedBB(distanceFromEdge, distanceFromEdge, 1 - height,
                        1 - distanceFromEdge, 1 - distanceFromEdge, 1);
            case SOUTH:
                return new AxisAlignedBB(distanceFromEdge, distanceFromEdge, 0,
                        1 - distanceFromEdge, 1 - distanceFromEdge, height);
            case WEST:
                return new AxisAlignedBB(1 - height, distanceFromEdge, distanceFromEdge,
                        1, 1 - distanceFromEdge, 1 - distanceFromEdge);
            case EAST:
                return new AxisAlignedBB(0, distanceFromEdge, distanceFromEdge,
                        height, 1 - distanceFromEdge, 1 - distanceFromEdge);
            default:
                return new AxisAlignedBB(distanceFromEdge, 0, distanceFromEdge,
                        1 - distanceFromEdge, height, 1 - distanceFromEdge);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = new Random();
        if(rand.nextInt(2) == 0) {
            drops.add(new ItemStack(ModItems.GAELON_SHARD, 1));
        }

    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 1;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float lookX, float lookY, float lookZ, int meta, EntityLivingBase player) {
        return this.getDefaultState().withProperty(FACING, facing);
    }

}
