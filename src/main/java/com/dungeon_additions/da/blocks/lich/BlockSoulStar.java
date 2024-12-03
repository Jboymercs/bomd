package com.dungeon_additions.da.blocks.lich;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.entity.tileEntity.TileEntityLichSpawner;
import com.google.common.base.Predicate;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSoulStar extends BlockBase implements ITileEntityProvider, IBlockUpdater {

    public static final PropertyEnum<EnumLichSpawner> STATE = PropertyEnum.create("state", EnumLichSpawner.class);
    int counter = 0;
    private Item activationItem;

    public BlockSoulStar(String name, Item item) {
        super(name, Material.ROCK, 1000, 10000, SoundType.STONE);
        this.activationItem = item;
        this.setBlockUnbreakable();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityLichSpawner)) return state;
        TileEntityLichSpawner spawner = (TileEntityLichSpawner) te;
        return state.withProperty(STATE, spawner.getState());
    }

    public String byState(IBlockState state) {
        return  "soul_star_block";
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLichSpawner();
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

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(STATE).getLightLevel();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STATE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity te = world.getTileEntity(pos);

        if(te instanceof TileEntityLichSpawner) {
            TileEntityLichSpawner spawner = (TileEntityLichSpawner) te;
            if(player.getHeldItemMainhand() != null && stack.getItem() == this.activationItem && !world.isRemote){
                if (spawner.getState() != EnumLichSpawner.ACTIVE) {
                    //Consume Item and set this state to active
                    player.getHeldItemMainhand().shrink(1);
                    spawner.setState(EnumLichSpawner.ACTIVE);
                    System.out.println("Sensing Correctly");
                    return true;
                }
            } else {
                player.sendStatusMessage(new TextComponentTranslation("da.lich_spawn.soul_star", new Object[0]), true);
            }
        }

        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);

    }
}
