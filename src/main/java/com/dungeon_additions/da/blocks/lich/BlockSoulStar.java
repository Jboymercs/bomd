package com.dungeon_additions.da.blocks.lich;

import com.dungeon_additions.da.blocks.BlockBase;
import com.dungeon_additions.da.blocks.base.IBlockUpdater;
import com.dungeon_additions.da.config.MobConfig;
import com.dungeon_additions.da.entity.tileEntity.TileEntityLichSpawner;
import com.dungeon_additions.da.items.ItemSoulStar;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSoulStar extends BlockBase implements ITileEntityProvider, IBlockUpdater {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    public static final PropertyEnum<EnumLichSpawner> STATE = PropertyEnum.create("state", EnumLichSpawner.class);
    int counter = 0;
    private Item activationItem;

    public BlockSoulStar(String name, Item item) {
        super(name, Material.ROCK, 1000, 10000, SoundType.STONE);
        this.activationItem = item;
        this.setBlockUnbreakable();
    }

    public BlockSoulStar(String name, Material material, float hardness, float resistance, SoundType soundType, Item item1) {
        super(name, material, hardness, resistance, soundType);
        this.activationItem = item1;
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
        TileEntity te = world.getTileEntity(pos);
        if(MobConfig.lich_enable_daylight && world.getWorldTime() < MobConfig.lich_summon_time) {
            player.sendStatusMessage(new TextComponentTranslation("da.lich_wrong_time", new Object[0]), true);
            return false;
        }
        if(te instanceof TileEntityLichSpawner) {
            TileEntityLichSpawner spawner = (TileEntityLichSpawner) te;
            if(player.getHeldItemMainhand().getItem() instanceof ItemSoulStar){
                if (spawner.getState() != EnumLichSpawner.ACTIVE) {
                    //Consume Item and set this state to active
                    world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundsHandler.SOUL_STAR_ACTIVATE, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.7F + 0.3F, false);
                    player.getHeldItemMainhand().shrink(1);
                    spawner.setState(EnumLichSpawner.ACTIVE);
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

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityLichSpawner) {
            TileEntityLichSpawner spawner = ((TileEntityLichSpawner) te);
            if (spawner.getState() == EnumLichSpawner.INACTIVE) {
                if (rand.nextInt(2) == 0) {
                    for(int i = 0; i < 20; i++) {
                        Vec3d particlePos = new Vec3d(pos.getX() + 0.4 + ModRand.getFloat(0.3F), pos.getY() + 1.5, pos.getZ() + 0.4 + ModRand.getFloat(0.3F));
                        ParticleManager.spawnColoredSmoke(worldIn, particlePos, ModColors.GREY, new Vec3d(0, -0.1, 0));
                    }
                }
            } else if (spawner.getState() == EnumLichSpawner.ACTIVE) {
                if (rand.nextInt(2) == 0) {
                    for(int i = 0; i < 20; i++) {
                        Vec3d particlePos = new Vec3d(pos.getX() + 0.4 + ModRand.getFloat(0.3F), pos.getY() + 0.6, pos.getZ() + 0.4 + ModRand.getFloat(0.3F));
                        ParticleManager.spawnColoredSmoke(worldIn, particlePos, ModColors.AZURE, new Vec3d(0, 0.1, 0));
                    }
                }
            }

        }
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }
}
