package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.item.EnumWeaponType;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.player.ActionDauntlessLazer;
import com.dungeon_additions.da.entity.player.ActionPlayerDauntlesLazerBarrage;
import com.dungeon_additions.da.entity.player.ActionPlayerHighAOE;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemNightfallSword extends ToolSword implements IAnimatable {
    private String info_loc;
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemNightfallSword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(1680);
        this.falter_value = 0.25F;
        this.weapon_type = EnumWeaponType.SWORD;
        this.swingSound = SoundsHandler.DAUNTLESS_SWING;
        this.swingRadius = 1F;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if(!ModConfig.combat_system_enabled && !ModConfig.weapon_hit_delays) {
            attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundsHandler.DAUNTLESS_SWING, SoundCategory.NEUTRAL, 0.4f, 0.6f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
        }
        if(attacker instanceof EntityPlayer) {
            if(!((EntityPlayer)attacker).getCooldownTracker().hasCooldown(stack.getItem())) {
                this.setAbilityVal(stack, this.getAbilityVal(stack) + 1);
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!worldIn.isRemote && this.getAbilityVal(stack) != 0 && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
            int multi_cooldown = (7 * 20) * this.getAbilityVal(stack);
            float damageModif = 8 + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1);
            boolean hasHelmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHTFALL_HELMET;
            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET) {
                damageModif = (float) (damageModif * ModConfig.incendium_helmet_multipler);
            }
            if(this.getAbilityVal(stack) == 1) {
                new ActionPlayerHighAOE(hasHelmet ? 4 : 3, 3, damageModif + 1).performAction(player);
            } else if (this.getAbilityVal(stack) == 2) {
                //summons lazer barrage
                new ActionPlayerDauntlesLazerBarrage(damageModif + ModUtils.addMageSetBonus(player, 0)).performAction(player);
            } else if (this.getAbilityVal(stack) >= 3) {
                //summons vertical lazers
                new ActionDauntlessLazer(damageModif + ModUtils.addMageSetBonus(player, 0)).performAction(player);
            }

            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.DAUNTLESS_USE_SWORD, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
            player.getCooldownTracker().setCooldown(this, multi_cooldown);
            stack.damageItem(this.getAbilityVal(stack), player);
            this.setAbilityVal(stack, 0);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!worldIn.isRemote)
        {
            if(stack.getItem() instanceof ItemNightfallSword) {
                if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("ability_sword")) {
                    this.setNBTonAbility(stack, 0);
                }
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public void setNBTonAbility(ItemStack stack, int val)
    {
        NBTTagCompound nbt;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ability_sword"))
        { nbt = stack.getTagCompound(); }
        else
        { nbt = new NBTTagCompound(); }
        nbt.setInteger("ability_sword", val);
        nbt.setInteger("weaponDelay", weaponDelay);
        stack.setTagCompound(nbt);
    }

    public int getAbilityVal(ItemStack stack) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("ability_sword")) {
            return stack.getTagCompound().getInteger("ability_sword");
        } else {
            return 0;
        }
    }

    public void setAbilityVal(ItemStack stack, int val) {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("ability_sword")) {
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setInteger("ability_sword", val);
            stack.setTagCompound(nbt);
        }
    }


    @Override
    public int getWeaponDelay(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("weaponDelay") ? stack.getTagCompound().getInteger("weaponDelay") + 1 : 0;
    }


    public int getSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int currentY = max;

        while(currentY >= min)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)) && !world.isRemote && world.getBlockState(pos.add(0, currentY, 0)).isFullBlock()) {
                return currentY;
            }

            currentY--;
        }

        return 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public double getAttackSpeed() {
        return -2.9000000953674316D;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
