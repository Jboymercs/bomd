package com.dungeon_additions.da.items.trinket;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.items.ItemBase;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemTrinket extends ItemBase {
    private String info_loc;
    public ItemTrinket(String name, String info_loc, int maxDamage) {
        super(name);
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.info_loc = info_loc;
        this.maxStackSize = 1;
        this.setMaxDamage(maxDamage);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.trinket_item.name"));
    }
}
