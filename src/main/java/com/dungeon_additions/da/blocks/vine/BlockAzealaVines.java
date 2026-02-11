package com.dungeon_additions.da.blocks.vine;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.handlers.BOMDSoundTypes;
import com.dungeon_additions.da.util.handlers.RegistryHandler;
import com.dungeon_additions.da.util.mapper.AdvancedStateMap;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockAzealaVines extends BlockBush implements IGrowable, IHasModel, RegistryHandler.IStateMappedBlock
{
    protected static final AxisAlignedBB VINE_MID = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);
    public static final PropertyBool IS_BOTTOM = PropertyBool.create("is_bottom");
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);

    public BlockAzealaVines(String name, Material materialIn) {
        super(materialIn);
        setTranslationKey(name);
        setRegistryName(name);
        setSoundType(BOMDSoundTypes.MOSS);
        this.setTickRandomly(true);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(IS_BOTTOM, true));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{AGE, IS_BOTTOM});
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    { return state.withProperty(IS_BOTTOM, isBottom(worldIn, pos)); }

    public boolean isBottom(IBlockAccess worldIn, BlockPos pos)
    { return worldIn.getBlockState(pos.down()).getBlock() != ModBlocks.AZAELA_VINES && worldIn.getBlockState(pos.down()).getBlock() != ModBlocks.AZAELA_BERRY_VINES ; }

    /** Drops are only needed for the Berry version of the block */
    public int quantityDropped(Random random)
    {
        return this == ModBlocks.AZAELA_BERRY_VINES ? 1 : 0;
    }

    /** Drops Glow Berries */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    { return ModItems.GLOW_BERRY; }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    { return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && canBlockStay(world, pos, world.getBlockState(pos)); }

    /** Overrides the Creative Player's Pick Block with the dropped item */
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World worldIn, BlockPos pos, EntityPlayer player)
    { return new ItemStack(this.getItemDropped(state, worldIn.rand, 0)); }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    { return worldIn.getBlockState(pos.up()).getBlockFaceShape(worldIn, pos.up(), EnumFacing.DOWN) == BlockFaceShape.SOLID || worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.AZAELA_VINES || worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.AZAELA_BERRY_VINES; }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (isBottom(worldIn, pos))
        {
            if (state.getValue(AGE) == 0) worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(worldIn.rand.nextInt(14 + 1))), 2);
        }
        else
        { worldIn.setBlockState(pos, state.withProperty(AGE, 0), 2); }
        this.checkAndDropBlock(worldIn, pos, state);
    }

    /** Interacting will always harvest the berries if this vine has berries, otherwise allows clipping the bottom with Shears */
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (this == ModBlocks.AZAELA_BERRY_VINES)
        {
            worldIn.setBlockState(pos, ModBlocks.AZAELA_VINES.getDefaultState().withProperty(AGE, 15), 2);
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
            spawnAsEntity(worldIn, pos, new ItemStack(this.getItemDropped(state, worldIn.rand, 0)));
            return true;
        }
        else if ((Integer)state.getValue(AGE).intValue() != 15 && isBottom(worldIn, pos))
        {
            if (item instanceof ItemShears)
            {
                worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, 15), 2);
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int currentAge = state.getValue(AGE);
        if(isBottom(worldIn, pos) && currentAge < 15 && rand.nextInt(16) == 0 && worldIn.isAirBlock(pos.down()))
        {
            growDown(worldIn, rand, pos, state, false);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
            if (rand.nextInt(29) == 0) {

                Main.proxy.spawnParticle(1, worldIn, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), worldIn.rand.nextFloat()/3 - worldIn.rand.nextFloat()/3, -0.04, worldIn.rand.nextFloat()/3 - worldIn.rand.nextFloat()/3, ModRand.range(40, 50));
            }
    }

    // TODO: Test in Multiplayer/over a Server!
    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
      //  if (entityIn instanceof EntityPlayer)
     //   {
     //       EntityPlayer player = (EntityPlayer) entityIn;
//
     //       if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
        //    {
      //          player.motionY = 0.2D;
        //        player.fallDistance = 0.0F;
       //     }
     //   }
    }

    @Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) { return false; }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return VINE_MID; }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    { return Block.EnumOffsetType.NONE; }

    @Override
    public void registerModels()
    { Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); }

    @Override
    public int getMetaFromState(IBlockState state)
    { return ((Integer)state.getValue(AGE)); }

    @Override
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(AGE, meta); }

    /** Randomize the Age when placed. */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        Random rand = worldIn.rand;
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(rand.nextInt(14 + 1)));
    }

    @Override
    public void setStateMapper(AdvancedStateMap.Builder builder) {
        builder.ignore(AGE);
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    { return this != ModBlocks.AZAELA_BERRY_VINES; }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    { return this != ModBlocks.AZAELA_BERRY_VINES; }

    /** Bonemealing only converts the vine into the Berry version */
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        int currentAge = worldIn.getBlockState(pos).getValue(AGE);

        if (this != ModBlocks.AZAELA_BERRY_VINES)
        {

            worldIn.setBlockState(pos, ModBlocks.AZAELA_BERRY_VINES.getDefaultState().withProperty(AGE, currentAge).withProperty(IS_BOTTOM, isBottom(worldIn, pos)));
        }
    }

    /**
     * Grows the Glow Berry/Cave Vines down by 1.
     * `randomizeAge` bool sets if the age of the added part is either Randomized, or the below Vine's Age +1
     * */
    public void growDown(World worldIn, Random rand, BlockPos pos, IBlockState state, boolean randomizeAge)
    {
        int currentAge = worldIn.getBlockState(pos).getValue(AGE);

        if(worldIn.isAirBlock(pos.down()))
        {
            /* First set this one's age to 0 */
            worldIn.setBlockState(pos, state.withProperty(AGE, 0));

            state = ModBlocks.AZAELA_VINES.getDefaultState();
            /** 11% chance to instead place berry vines! Wowy! */
            if (rand.nextInt(100) < 11) state = ModBlocks.AZAELA_BERRY_VINES.getDefaultState();
            
            if (randomizeAge)
            { worldIn.setBlockState(pos.down(), state.withProperty(AGE, Integer.valueOf(worldIn.rand.nextInt(14 + 1))), 2); }
            else
            { worldIn.setBlockState(pos.down(), state.withProperty(AGE, currentAge + 1), 2); }
        }
    }
}