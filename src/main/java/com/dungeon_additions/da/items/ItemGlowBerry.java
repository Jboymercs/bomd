package com.dungeon_additions.da.items;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModBlocks;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;


public class ItemGlowBerry extends ItemFood implements IHasModel, IPlantable {

    private final Block crops;
    public ItemGlowBerry(String name, int amount, float saturation, boolean isWolfFood, Block crops) {
        super(amount, saturation, isWolfFood);
        setTranslationKey(name);
        this.crops = crops;

        setRegistryName(name);
        ModItems.ITEMS.add(this);
    }


    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        boolean isReplaceable = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        BlockPos blockpos = isReplaceable ? pos : pos.offset(facing);

        if (facing == EnumFacing.DOWN && player.canPlayerEdit(blockpos, facing, itemstack) && crops.canPlaceBlockAt(worldIn, blockpos))
        {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(blockpos, this.crops.getDefaultState());
                worldIn.playSound((EntityPlayer) null, pos, SoundsHandler.MOSS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (!player.capabilities.isCreativeMode) itemstack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.crops.getDefaultState();
    }
}
