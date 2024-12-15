package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.entity.tileEntity.TileEntityLevitationBlock;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockLevitationAltar extends BlockBase implements ITileEntityProvider {

    private String info_loc;

    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");

    public BlockLevitationAltar(String name, Material material) {
        super(name, material);
        setDefaultState(blockState.getBaseState().withProperty(TRIGGERED, Boolean.valueOf(false)));
    }

    public BlockLevitationAltar(String name, Material material, float hardness, float resistance, SoundType soundType, String info_loc) {
        super(name, material, hardness, resistance, soundType);
        this.info_loc = info_loc;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
        boolean flag1 = state.getValue(TRIGGERED).booleanValue();

        if (flag && !flag1) {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)));
        } else if (!flag && flag1) {
             worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)));
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf(false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TRIGGERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (state.getValue(TRIGGERED).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(TRIGGERED).booleanValue()) {
            Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 1.2, pos.getZ() -0.1 + ModRand.getFloat(1.1F));
            ParticleManager.spawnDust(worldIn, particlePos, ModColors.RED, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
        } else  {
            Vec3d particlePos = new Vec3d(pos.getX() -0.1 + ModRand.getFloat(1.1F), pos.getY() + 1.2, pos.getZ() -0.1 + ModRand.getFloat(1.1F));
            ParticleManager.spawnDust(worldIn, particlePos, ModColors.AZURE, new Vec3d(0, 0.05, 0), ModRand.range(10, 15));
        }
    }
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TRIGGERED, Boolean.valueOf((meta & 8) > 0));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLevitationBlock();
    }


}
