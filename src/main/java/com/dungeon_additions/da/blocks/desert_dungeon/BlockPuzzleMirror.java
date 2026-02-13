package com.dungeon_additions.da.blocks.desert_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.entity.tileEntity.TileEntityPuzzleMirror;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockPuzzleMirror extends BlockContainer implements IHasModel, IBlockUpdater {

    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.9D, 0.8D);

    public BlockPuzzleMirror(String name, Material material) {
        super(material);
    }

    public BlockPuzzleMirror(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(material);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);
        this.setCreativeTab(DungeonAdditionsTab.BLOCKS);
        setTranslationKey(name);
        setRegistryName(name);
        this.hasTileEntity = true;
        this.setBlockUnbreakable();

        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }


    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(pos);
        if(!world.isRemote)
        {
            long time = world.getTotalWorldTime();

            if (te instanceof TileEntityPuzzleMirror)
            {
                TileEntityPuzzleMirror mirror = ((TileEntityPuzzleMirror) te);
                if (player.getHeldItemMainhand().isEmpty() && time > mirror.lastInteractTime)
                {
                    world.playSound(null, pos, SoundsHandler.MIRROR_MOVE, SoundCategory.BLOCKS, 0.4F, world.rand.nextFloat() * 0.8F + 0.3F);
                    mirror.addRotationTooSkull();
                    mirror.lastInteractTime = time + 5;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPuzzleMirror();
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public void update(World world, BlockPos pos) {}
}
