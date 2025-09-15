package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemKopis extends ToolSword {
    private String info_loc;

    public ItemKopis(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.info_loc = info_loc;
        this.setMaxDamage(1200);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if (attacker.world.isRemote) return false;
        int axeCoolDown = (int) (1.7 * 20);
        stack.damageItem(1, attacker);
        float realAttackDamage = this.getAttackDamage();
        double playerVec = (Math.abs(attacker.motionX + attacker.motionZ + attacker.motionY) * 12) - 1.5;
        System.out.println("Player Vec at" + playerVec);

        if(playerVec > 8) {
            playerVec = 8;
        }
        float regular_damage = (float) (realAttackDamage + 1 + (playerVec));
        //add some sort of cap to the motion

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            if(!player.getCooldownTracker().hasCooldown(this) && !player.onGround) {
                int potionBonus = 0;
                if(player.isPotionActive(MobEffects.SPEED)) {
                    potionBonus = 2;
                }
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker), (float) regular_damage + potionBonus);
            }
            player.getCooldownTracker().setCooldown(this, axeCoolDown);
        }


        return true;
    }


    @Override
    protected double getAttackSpeed() {
        return -2.3000000953674316D;
    }
}
