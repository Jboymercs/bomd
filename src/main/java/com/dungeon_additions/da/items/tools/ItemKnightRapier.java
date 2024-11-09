package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemKnightRapier extends ToolSword{

    private String info_loc;

    public ItemKnightRapier(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(502);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }


    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.2000000953674316D;
    }
}
