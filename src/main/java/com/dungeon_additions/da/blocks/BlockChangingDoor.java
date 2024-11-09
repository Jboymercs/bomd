package com.dungeon_additions.da.blocks;

import com.sun.jna.platform.win32.Winspool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockChangingDoor extends BlockBase {

    protected static final AxisAlignedBB MINI = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    public static final PropertyBool ACTIVE = PropertyBool.create("activedoor");


    public BlockChangingDoor(String name, Material material) {
        super(name, material);
    }

    public BlockChangingDoor(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
        this.useNeighborBrightness = true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{ACTIVE});
    }


    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos) == this.blockState.getBaseState().withProperty(ACTIVE, true);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos) == blockState.withProperty(ACTIVE, true) ? NULL_AABB : FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return source.getBlockState(pos) == state.withProperty(ACTIVE, true) ? MINI : FULL_BLOCK_AABB;
    }

    private boolean setForActivation;

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(worldIn.isRemote) {
            return;
        }
        if(worldIn.isBlockPowered(pos)) {
            worldIn.scheduleUpdate(pos, this, 100);
            worldIn.setBlockState(pos, state.withProperty(ACTIVE, true));
        } else if (worldIn.getBlockState(pos.up()) == this.blockState.getBaseState().withProperty(ACTIVE, true) || worldIn.getBlockState(pos.down()) == this.blockState.getBaseState().withProperty(ACTIVE, true) ||
                worldIn.getBlockState(pos.add(1,0,0)) == this.blockState.getBaseState().withProperty(ACTIVE, true) || worldIn.getBlockState(pos.add(0,0,1)) == this.blockState.getBaseState().withProperty(ACTIVE, true) ||
                worldIn.getBlockState(pos.add(-1,0,0)) == this.blockState.getBaseState().withProperty(ACTIVE, true) || worldIn.getBlockState(pos.add(0,0,-1)) == this.blockState.getBaseState().withProperty(ACTIVE, true)) {
            worldIn.scheduleUpdate(pos, this, 5);
            this.setForActivation = true;

        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!state.getValue(ACTIVE)) {

            world.scheduleUpdate(pos, this, 100);
          return world.setBlockState(pos, state.withProperty(ACTIVE, true));

        }

        return false;
    }

    @Override
    public int tickRate(World world) {
        return 15;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if(world.isRemote) {
            return;
        }
        if(this.setForActivation && world.getBlockState(pos) == this.blockState.getBaseState().withProperty(ACTIVE, false)) {
            world.setBlockState(pos, state.withProperty(ACTIVE, true));
            world.scheduleUpdate(pos, this, 100);
        }
         else if(world.getBlockState(pos) == state.withProperty(ACTIVE, true)) {
                world.setBlockState(pos, state.withProperty(ACTIVE, false));
                this.setForActivation = false;
            }

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta == 1);
    }
}
