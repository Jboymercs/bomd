package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.animation.AzLib.DaItemAnimDispatcher;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.generic.EntityDelayedExplosion;
import com.dungeon_additions.da.entity.night_lich.ProjectileMagicMissile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemMageStaff extends ToolSword {
    public final DaItemAnimDispatcher animator;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_FIGHT_MODE = "fight";
    private String info_loc;
    public boolean isMeleeMode;
    private int ticksSaved;

    public ItemMageStaff(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(936);
        this.falter_value = 0;
        this.swingRadius = 0.25F;
        this.animator = new DaItemAnimDispatcher();
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
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        if(this.isMeleeMode) {
            attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.8f, 0.3f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
            if (attacker.world.isRemote) return false;
            int damage_bonus = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
            int damage_bonus_2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
            int damage_bonus_3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
            float damage = (float) (this.getAttackDamage() + ModConfig.combat_wand_melee_damage + damage_bonus + (damage_bonus_2 * 0.5) + (damage_bonus_3 * 0.5));
            if(attacker instanceof EntityPlayer) {
                EntityPlayer player = ((EntityPlayer) attacker);
                target.attackEntityFrom(ModUtils.causeAxeDamage(attacker), (float) damage);
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = 10 * 20;

        if (!worldIn.isRemote)
        {
            if (stack.getItem() == this && !this.isMeleeMode) {
                animator.performItemAnimation(player, stack, true, ANIM_IDLE);
            }
        }

        //melee mode
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            //explosion attack
            if(player.isSneaking()) {
                int damage_bonus = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
                int damage_bonus_2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
                int damage_bonus_3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
                EntityDelayedExplosion delayedExplosion = new EntityDelayedExplosion(worldIn, player, (float) (this.getAttackDamage() + ModConfig.combat_wand_explosion_damage + damage_bonus + (damage_bonus_2 * 0.5) + (damage_bonus_3 * 0.5)) + ModUtils.addMageSetBonus(player, 0, 1.5F));
                Vec3d playerLookVec = player.getLookVec();
                Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 5D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 5D);
                delayedExplosion.setPosition(playerPos.x, playerPos.y, playerPos.z);
                worldIn.spawnEntity(delayedExplosion);
                player.getCooldownTracker().setCooldown(this, (int) (SwordCoolDown * 0.75));
                stack.damageItem(4, player);
            } else {
                animator.performItemAnimation(player, stack, false, ANIM_FIGHT_MODE);
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.APATHYR_SLIGHT_DASH, SoundCategory.NEUTRAL, 1.0f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.4f));
                this.isMeleeMode = true;
                this.ticksSaved = player.ticksExisted;
                player.getCooldownTracker().setCooldown(this, SwordCoolDown);
                stack.damageItem(2, player);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(this.isMeleeMode && ticksSaved + 160 < player.ticksExisted) {
                this.isMeleeMode = false;
            } else if(!player.getCooldownTracker().hasCooldown(this) && player.isSwingInProgress && player.getHeldItemMainhand().getItem() == this) {
                int damage_bonus = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
                int damage_bonus_2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
                int damage_bonus_3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
                ProjectileMagicMissile missile = new ProjectileMagicMissile(worldIn, player, (float) (ModConfig.combat_wand_projectile_damage + damage_bonus + (damage_bonus_2 * 0.5) + (damage_bonus_3 * 0.5)) + ModUtils.addMageSetBonus(player, 0));
                Vec3d playerLookVec = player.getLookVec();
                Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
                missile.setPosition(playerPos.x, playerPos.y, playerPos.z);
                missile.setTravelRange(17);
                missile.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.9f, 1.0f);
                worldIn.spawnEntity(missile);
                stack.damageItem(1, player);
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.WYRK_STAFF_SHOOT, SoundCategory.NEUTRAL, 0.6f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                player.getCooldownTracker().setCooldown(this, 30);
            }
        }
    }

    public void onEnemyHit(EntityLivingBase user, EntityLivingBase enemy, Vec3d rammingDir) {
        double attackDamage = (double) (this.getAttackDamage() + 1);
        if(user instanceof EntityPlayer) {
            if(user.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET) {
                attackDamage = (double) (this.getAttackDamage() + 1) * ModConfig.incendium_helmet_multipler;
            }
        }
        enemy.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)user), (float) attackDamage);
        enemy.knockBack(user, 1.5F, rammingDir.x, rammingDir.z);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }

    @Override
    public double getAttackSpeed() {
        return -3.0000000953674316D;
    }
}
