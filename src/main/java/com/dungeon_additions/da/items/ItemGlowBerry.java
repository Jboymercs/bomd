package com.dungeon_additions.da.items;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.List;

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
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.DOWN && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().isFullBlock(state) && worldIn.isAirBlock(pos.down()) ||
        facing == EnumFacing.DOWN && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock() == crops && worldIn.isAirBlock(pos.down()))
        {
            worldIn.setBlockState(pos.down(), this.crops.getDefaultState(), 11);
            worldIn.playSound(player, pos, SoundsHandler.MOSS_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {

        return super.onItemUseFinish(itemStack, world, living);
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
