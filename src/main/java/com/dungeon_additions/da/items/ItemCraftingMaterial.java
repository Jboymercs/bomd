package com.dungeon_additions.da.items;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemCraftingMaterial extends ItemBase {

    private String info_loc;

    public ItemCraftingMaterial(String name, String info_loc) {
        super(name);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.info_loc = info_loc;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(ModConfig.enable_crafting_tooltips) {
            tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        }
    }
}
