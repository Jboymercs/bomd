package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.desert_dungeon.ProjectileThousandCuts;
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
import net.minecraft.util.text.translation.I18n;
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
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundsHandler.WARLORD_SWING, SoundCategory.NEUTRAL, 0.6f, 0.5f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
        if (attacker.world.isRemote) return false;
        int axeCoolDown = (int) (ModConfig.kopis_cooldown * 20);
        stack.damageItem(1, attacker);
        float realAttackDamage = this.getAttackDamage();
        double playerVec = (Math.abs(attacker.motionX + attacker.motionZ + attacker.motionY) * 12) - 1.5;

       // System.out.println("Player Vec at" + playerVec);
        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            if(!player.getCooldownTracker().hasCooldown(this) && !player.onGround && playerVec > 1.59) {
                float potionBonus = ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1);
                if(player.isPotionActive(MobEffects.SPEED)) {
                    potionBonus = 2 + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1);
                }
                ProjectileThousandCuts thousandCuts = new ProjectileThousandCuts(attacker.world, player, 4 + potionBonus);
                if(target != null) {
                    thousandCuts.setPosition(target.posX, target.posY + 1.5, target.posZ);
                    player.world.spawnEntity(thousandCuts);
                    target.motionX = 0;
                    target.motionZ = 0;
                    player.getCooldownTracker().setCooldown(this, axeCoolDown);
                }
            }
        }


        return true;
    }


    @Override
    protected double getAttackSpeed() {
        return -2.1000000953674316D;
    }
}
