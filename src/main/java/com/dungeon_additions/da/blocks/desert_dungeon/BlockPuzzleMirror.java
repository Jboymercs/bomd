package com.dungeon_additions.da.blocks.desert_dungeon;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.entity.tileEntity.TileEntityLichSpawner;
import com.dungeon_additions.da.entity.tileEntity.TileEntityPuzzleMirror;
import com.dungeon_additions.da.entity.tileEntity.TileEntitySporeBlossom;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockPuzzleMirror extends BlockContainer implements IHasModel, IBlockUpdater {

    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.9D, 0.8D);

    int counter = 0;

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


    private int delay = 0;
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(pos);
        if(!world.isRemote && delay <= 0)
        {
            delay = 5;
            if (te instanceof TileEntityPuzzleMirror)
            {
                TileEntityPuzzleMirror mirror = ((TileEntityPuzzleMirror) te);
                if (player.getHeldItemMainhand().isEmpty())
                {
                    world.playSound(null, pos, SoundsHandler.MIRROR_MOVE, SoundCategory.BLOCKS, 0.4F, world.rand.nextFloat() * 0.8F + 0.3F);
                    mirror.addRotationTooSkull();
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
    public void update(World world, BlockPos pos)
    {
        counter++;
        delay--;
        if (counter % 5 == 0)
        {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>()
            {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
                }
            });

        }
    }
}
