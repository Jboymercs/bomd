package com.dungeon_additions.da.items;

import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemAmbitionFlame extends ItemBase{
    private String info_loc;

    public ItemAmbitionFlame(String name, String info_loc) {
        super(name);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.info_loc = info_loc;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.RED + ModUtils.translateDesc(info_loc));
    }
}
