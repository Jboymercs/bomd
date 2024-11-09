package com.dungeon_additions.da.items;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemFoodBase extends ItemFood implements IHasModel {
    private String info_loc;
    public ItemFoodBase(String name, String info_loc, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setTranslationKey(name);
        this.info_loc = info_loc;
        setRegistryName(name);
        ModItems.ITEMS.add(this);
    }


    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
        if (living instanceof EntityPlayer && !world.isRemote) {
            living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 1));
            living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
        }
        return super.onItemUseFinish(itemStack, world, living);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
