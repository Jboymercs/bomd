package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.dark_dungeon.dauntless.ProjectileDauntlessFist;
import com.dungeon_additions.da.entity.mini_blossom.EntityDart;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.items.armor.ModIncendiumHelmet;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemNightfallGauntlets extends ToolSword implements IAnimatable {
    private String info_loc;
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemNightfallGauntlets(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.info_loc = info_loc;
        this.setMaxDamage(1320);
        this.falter_value = 0.1F;
        this.weapon_type = EnumWeaponType.DAGGER;
        this.weaponReach -= 1F;
        this.swingSound = SoundsHandler.DAUNTLESS_PUNCH;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) attacker);
            boolean hasOffhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == ModItems.NIGHTFALL_GAUNTLETS && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.NIGHTFALL_GAUNTLETS;
            if(hasOffhand && !attacker.world.isRemote) {
                target.addPotionEffect(new PotionEffect(ModPotions.HEMORRHAGE, 160, 0, false, false));
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 7 * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            float inaccuracy = 0.0f;
            float speed = 1.2f;
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.DAUNTLESS_PUNCH, SoundCategory.NEUTRAL, 0.7f, 0.3f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            // Shoots projectiles in a small arc
                float hasOffhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == ModItems.NIGHTFALL_GAUNTLETS && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.NIGHTFALL_GAUNTLETS? 2 : 0;
                ProjectileDauntlessFist projectile = new ProjectileDauntlessFist(worldIn, player, 9 + ModUtils.addMageSetBonus(player, 0, 1.5F) + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1) + hasOffhand);
                projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, speed, inaccuracy);
                projectile.rotationYaw = player.rotationYaw;
                projectile.rotationPitch = player.rotationPitch;
                projectile.setTravelRange(12F);
                player.world.spawnEntity(projectile);
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
            stack.damageItem(1, player);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


    @Override
    public double getAttackSpeed() {
        return -2.1000000953674316D;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
