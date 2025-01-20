package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemDraugrSword extends ToolSword{

    String info_loc;


    public ItemDraugrSword(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(874);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 160, 0));
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.4000000953674316D;
    }
}
