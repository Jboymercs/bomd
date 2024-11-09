package com.dungeon_additions.da.items;

import com.dungeon_additions.da.entity.ProjectileEndlessEnderpearl;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemEndlessEnderpearl extends ItemBase{

    private String info_loc;
    public ItemEndlessEnderpearl(String name, String info_loc) {
        super(name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.maxStackSize = 1;
        this.info_loc = info_loc;

    }


    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.2F / (itemRand.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldownTracker().setCooldown(this, 20);

        if (!worldIn.isRemote)
        {
            ProjectileEndlessEnderpearl entityenderpearl = new ProjectileEndlessEnderpearl(worldIn, playerIn);
            entityenderpearl.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entityenderpearl);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }


    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }
}
