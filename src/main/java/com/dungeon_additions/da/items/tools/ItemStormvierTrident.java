package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.rot_knights.ProjectileStormTrident;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.IHasModel;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemStormvierTrident extends ToolSword implements IAnimatable {

    private String info_loc;
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemStormvierTrident(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
        this.setMaxDamage(1200);
        this.weapon_type = EnumWeaponType.SPEAR;
        this.weaponReach += 1.25F;
        this.swingSound = SoundsHandler.SWING_FAST;
        this.falter_value = 0.12F;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if(!playerIn.getCooldownTracker().hasCooldown(this)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase living, int timeLeft)
    {

        if (living instanceof EntityPlayer)
        {
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            float f = getArrowVelocity(i);
            if (i < 20 || f <= 0.59) return;

            float velocity = (i * 0.015F);
            if(velocity > 1.5) {
                velocity = 1.5F;
            }
            living.world.playSound(null, living.getPosition(), SoundsHandler.ROT_TRIDENT_THROW, living.getSoundCategory(), 1.0F, 1.0F);
            if (living instanceof EntityPlayer && !((EntityPlayer)living).capabilities.isCreativeMode)  living.swingArm(EnumHand.MAIN_HAND);

            if (!worldIn.isRemote)
            {
                float damage = (this.getAttackDamage() + 1) + ModUtils.addAbilityBonusDamage(((EntityPlayer)living).getHeldItemMainhand(), 1.5F);
                ProjectileStormTrident trident = new ProjectileStormTrident(worldIn, living, damage * velocity, 0.8F * velocity);
                trident.shoot(living, living.rotationPitch, living.rotationYaw, 0.0F, velocity + 1, 1.0F);
                if (living instanceof EntityPlayer && ((EntityPlayer)living).capabilities.isCreativeMode) {
                    trident.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                } else {
                    trident.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
                }
                living.world.spawnEntity(trident);
                trident.setNoGravity(false);
                stack.damageItem(2, living);
                ((EntityPlayer)living).getCooldownTracker().setCooldown(this, (int) (200 * velocity));
            }
        }
    }


    public static float getArrowVelocity(int charge)
    {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public double getAttackSpeed() {
        return -3.0000000953674316D;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
