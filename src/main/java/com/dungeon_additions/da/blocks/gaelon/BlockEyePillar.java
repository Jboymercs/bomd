package com.dungeon_additions.da.blocks.gaelon;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.entity.tileEntity.TileEntityEyePillar;
import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockEyePillar extends BlockBase implements ITileEntityProvider {

    public BlockEyePillar(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEyePillar();
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        return null;
    }

    //reset the pillar blocks
    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, ModBlocks.DARK_GLOW_LIT_PILLAR.getDefaultState());
    }
}
