package com.dungeon_additions.da.blocks.void_blocks;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.entity.tileEntity.TileEntityObsidilithRune;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockObsdilithRune extends BlockBase implements ITileEntityProvider {
    public BlockObsdilithRune(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityObsidilithRune();
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        return null;
    }
}
