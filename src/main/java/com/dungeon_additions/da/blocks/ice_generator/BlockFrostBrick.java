package com.dungeon_additions.da.blocks.ice_generator;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.entity.tileEntity.TileEntityFrostBrick;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFrostBrick extends BlockBase implements ITileEntityProvider {

    private String info_loc;

    public BlockFrostBrick(String name, Material material) {
        super(name, material);
    }

    public BlockFrostBrick(String name, String info_loc, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFrostBrick();
    }
}
