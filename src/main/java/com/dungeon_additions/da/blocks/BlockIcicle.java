package com.dungeon_additions.da.blocks;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.blossom.EntityAbstractVoidBlossom;
import com.dungeon_additions.da.entity.frost_dungeon.EntityFrostBase;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModColors;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.ParticleManager;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockIcicle extends BlockFalling implements IHasModel {

    protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.2D, 0.1D, 0.2D, 0.8D, 1.0D, 0.8D);

    public BlockIcicle() {
        super(Material.GLASS);

    }

    private int standByTick = 0;

    public BlockIcicle(String name, float hardness, float resistance, SoundType soundType) {
        super(Material.GLASS);
        setTranslationKey(name);
        setRegistryName(name);
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(soundType);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) * 120);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if(worldIn.getBlockState(pos.add(0, 1, 0)) == Blocks.AIR.getDefaultState()) {
                this.checkFallable(worldIn, pos, true);
            } else {
                this.checkFallable(worldIn, pos, false);
            }
        }
    }

    private void checkFallable(World worldIn, BlockPos pos, boolean fall_current)
    {
         boolean fallInstantly = false;
         boolean fallFromPlayers = false;

        if(!worldIn.isRemote) {
            if(standByTick > 50) {
                AxisAlignedBB box = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                List<EntityFrostBase> nearbyBoss = worldIn.getEntitiesWithinAABB(EntityFrostBase.class, box.grow(20D));
                List<EntityPlayer> nearbyPlayer = worldIn.getEntitiesWithinAABB(EntityPlayer.class, box.grow(4D, 12D, 4D));
                if (!nearbyBoss.isEmpty()) {
                    for (EntityFrostBase base : nearbyBoss) {
                        //only when the mob is in this state can icicles drop
                        if (base.setUpdateIcicles) {
                            fallInstantly = true;
                        }
                    }
                }


                if (!nearbyPlayer.isEmpty()) {
                    for (EntityPlayer player : nearbyPlayer) {
                        if (player.isSprinting() && !player.isCreative() && !player.isSpectator()) {
                            fallFromPlayers = true;
                        }
                    }
                }
            } else {
                standByTick++;
            }

            if(worldIn.isAreaLoaded(pos.add(-16, -16, -16), pos.add(16, 16, 16))) {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            }
        }
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
        {

            if (worldIn.isAreaLoaded(pos.add(-16, -16, -16), pos.add(16, 16, 16)))
            {
                if (worldIn.rand.nextInt(3) == 0 && fallInstantly || worldIn.rand.nextInt(5) == 0 && fallFromPlayers || fall_current)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.playSound(null, pos, SoundsHandler.ICICLE_DROP, SoundCategory.BLOCKS,
                            1.5F, 0.5f + worldIn.rand.nextFloat() * 1.2f);
                    worldIn.spawnEntity(entityfallingblock);
                }
            }
        }
    }


    protected void onStartFalling(EntityFallingBlock fallingEntity)
    {
        fallingEntity.setHurtEntities(true);
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, IBlockState fallingState, IBlockState hitState)
    {
        worldIn.setBlockToAir(pos);
        worldIn.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS,
                1, 0.5f + worldIn.rand.nextFloat() * 1.2f);


    }



    @SideOnly(Side.CLIENT)
    private void doEndParticles(World world, Vec3d posFrom) {
        ModUtils.circleCallback(1, 20, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y);
            ParticleManager.spawnDust(world, posFrom.add(0.5D, 0.15D, 0.5D), ModColors.WHITE, pos.normalize().scale(0.1), ModRand.range(15, 20));
        });
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 2;
    }

    public static boolean canFallThrough(IBlockState state)
    {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CRYSTAL_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.NONE;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
