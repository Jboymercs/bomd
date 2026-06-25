package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class ItemDraugrSword extends ToolSword{

    String info_loc;
    public ItemDraugrSword(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(1074);
        this.weapon_type = EnumWeaponType.SWORD;
        this.swingSound = SoundsHandler.SWING_REGULAR;
        this.swingRadius = 0.75F;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        int damage = 160;
        if(attacker instanceof EntityPlayer) {
         damage = ((EntityPlayer)attacker).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET ? (int) (160 * ModConfig.incendium_helmet_multipler): 160;
        }
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, damage, 0));
        stack.damageItem(1, attacker);
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public double getAttackSpeed() {
        return -2.4000000953674316D;
    }
}
