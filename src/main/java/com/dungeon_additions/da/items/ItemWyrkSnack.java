package com.dungeon_additions.da.items;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.frost_dungeon.EntityWyrk;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemWyrkSnack extends ItemFood implements IHasModel {

    private String info_loc;

    public ItemWyrkSnack(String name, String info_loc, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setTranslationKey(name);
        setRegistryName(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(250);
        ModItems.ITEMS.add(this);
        this.info_loc = info_loc;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.damageItem(1, entityLiving);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }


    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
