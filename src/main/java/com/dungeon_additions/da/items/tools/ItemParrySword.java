package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.projectiles.Projectile;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.tab.DungeonAdditionsTab;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.tools.Tool;
import java.util.List;

public class ItemParrySword extends ToolSword {

    private boolean isParrying = false;
    private boolean dealtDamage = false;
    private int currentLife = 0;

    private double setPlayerLife = 0;
    String info_loc;

    public ItemParrySword(String name, String info_loc, ToolMaterial material) {
        super(name, material);
        this.info_loc = info_loc;
        this.setCreativeTab(DungeonAdditionsTab.ALL);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        if (this.isParrying) return EnumAction.BLOCK;
        return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int SwordCoolDown = ModConfig.master_parry_sword_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this) && player.hurtTime == 0) {
            this.setPlayerLife = 0;
            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.6f, 0.3f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
            this.currentLife = player.ticksExisted;
            this.dealtDamage = false;
            this.isParrying = true;
            player.hurtResistantTime = 0;
            this.setPlayerLife = player.getHealth();
            player.setActiveHand(hand);
            stack.damageItem(1, player);
            player.getCooldownTracker().setCooldown(this, SwordCoolDown);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityPlayer && isParrying && !worldIn.isRemote) {
            EntityPlayer player = ((EntityPlayer) entityIn);
            boolean didBlockDamage = false;

            if(player.ticksExisted < currentLife + 10) {
                if(player.hurtTime > 0) {
                    didBlockDamage = true;
                }


                if(didBlockDamage && !this.dealtDamage) {
                    EntityLivingBase attacker = player.getAttackingEntity();
                    assert attacker != null;
                    float damage = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET ? (float) (8 * ModConfig.incendium_helmet_multipler) + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 2): 8 + ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 2);
                    attacker.attackEntityFrom(ModUtils.causeAxeDamage(player).setDamageBypassesArmor(), damage);
                    attacker.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
                    worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.IMPERIAL_SWORD_PARRY, SoundCategory.NEUTRAL, 1.0f, 0.8f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
                    this.isParrying = false;
                    player.stopActiveHand();
                    player.setHealth((float) this.setPlayerLife);
                    currentLife = 0;
                    ModUtils.performNTimes(6, (i) -> {
                        Vec3d playerLookVec = player.getLookVec();
                        Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 0.7D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 0.7D);
                        Main.proxy.spawnParticle(28, worldIn, playerPos.x + ModRand.getFloat(0.5F), playerPos.y + ModRand.getFloat(0.5F), playerPos.z + ModRand.getFloat(0.5F), 0,0,0);
                    });
                }
            } else {
                player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 240, 1, false, false));
                player.getCooldownTracker().setCooldown(this, ModConfig.master_parry_sword_cooldown * 20);
                this.isParrying = false;
                player.stopActiveHand();
                this.setPlayerLife = 0;
                currentLife = 0;
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    @Override
    protected double getAttackSpeed() {
        return -2.5000000953674316D;
    }
}
