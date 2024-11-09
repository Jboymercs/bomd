package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.tileEntity.TileEntityDisappearingSpawner;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDisappearingSpawner extends BlockContainer implements IHasModel {
    public BlockDisappearingSpawner(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setHardness(1000);
        setResistance(1000);
        this.setBlockUnbreakable();
        this.setSoundType(SoundType.STONE);

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        this.hasTileEntity = true;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    /**
     * Tile Entity methods to make the tile entity spawner work
     */
    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDisappearingSpawner();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model,
     * MODELBLOCK_ANIMATED for TESR-only, LIQUID for vanilla liquids, INVISIBLE to
     * skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
