package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.entity.tileEntity.TileEntityUpdaterFrost;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.keys.ItemFrostKey;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

public class BlockFrostKeyBlock extends BlockBase implements IBlockUpdater, ITileEntityProvider {
    private final Item activationItem = ModItems.FROST_KEY;
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    int counter = 0;
    BiFunction<World, BlockPos, Entity> spawnPortal;


    public BlockFrostKeyBlock(String name, Item item, BiFunction<World, BlockPos, Entity> spawnPortal) {
        super(name, Material.ROCK, 1000, 10000, SoundType.STONE);
        this.setBlockUnbreakable();
        this.hasTileEntity = true;
        this.spawnPortal = spawnPortal;
        this.setCreativeTab(DungeonAdditionsTab.BLOCKS);
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void update(World world, BlockPos pos) {
        counter++;
        if (counter % 5 == 0) {
            List<EntityPlayerSP> list = world.<EntityPlayerSP>getPlayers(EntityPlayerSP.class, new Predicate<EntityPlayerSP>() {
                @Override
                public boolean apply(@Nullable EntityPlayerSP player) {
                    return player.getHeldItem(EnumHand.MAIN_HAND).getItem() == activationItem;
                }
            });

        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityUpdaterFrost();
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        if(worldIn.getBlockState(pos.up()).isFullBlock()) {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
        }
        worldIn.removeTileEntity(pos);

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY,
                                    float hitZ) {

        if (playerIn.getHeldItemMainhand().getItem() == this.activationItem && !worldIn.isRemote || playerIn.getHeldItemMainhand().getItem() instanceof ItemFrostKey && !worldIn.isRemote) {

            playerIn.getHeldItem(hand).shrink(1);
            worldIn.spawnEntity(this.spawnPortal.apply(worldIn, pos));
            worldIn.setBlockToAir(pos);
            worldIn.setBlockToAir(pos.up());
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
