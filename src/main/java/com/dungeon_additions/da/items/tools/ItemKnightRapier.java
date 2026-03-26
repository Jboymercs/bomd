package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import java.util.List;

public class ItemKnightRapier extends ToolSword{

    private String info_loc;

    private boolean isParrying = false;
    private int currentLife = 0;
    private double setPlayerLife = 0;
    private boolean dealtDamage = false;

    public ItemKnightRapier(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
        this.setMaxDamage(502);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        if(ModConfig.enable_scaling_tooltips) {
            tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this) && player.hurtTime == 0) {
                this.setPlayerLife = 0;
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.6f, 0.3f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
                this.currentLife = player.ticksExisted;
                this.dealtDamage = false;
                this.isParrying = true;
                player.hurtResistantTime = 0;
                this.setPlayerLife = player.getHealth();
                player.setActiveHand(hand);
                player.getCooldownTracker().setCooldown(this, 200);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer player = ((EntityPlayer) entityIn);

                 if (player.getHeldItemMainhand().getItem() == this && isParrying) {
                boolean didBlockDamage = false;

                if (player.ticksExisted < currentLife + 10) {
                    if (player.hurtTime > 0) {
                        didBlockDamage = true;
                    }


                    if (didBlockDamage && !this.dealtDamage) {
                        EntityLivingBase attacker = player.getAttackingEntity();
                        assert attacker != null;
                        if(attacker instanceof EntityLivingBase) {
                            attacker.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 0, false, false));
                        }
                        worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.IMPERIAL_SWORD_PARRY, SoundCategory.NEUTRAL, 1.0f, 0.8f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
                        this.isParrying = false;
                        player.stopActiveHand();
                        float chance = (float) (0.1 + (ModUtils.addAbilityBonusDamage(player.getHeldItemMainhand(), 1.5F) * 0.07));
                        System.out.println("chance is at" + chance);
                        if(chance >= worldIn.rand.nextFloat()) {
                            player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 100, 0));
                        }
                        player.setHealth((float) this.setPlayerLife);
                        currentLife = 0;
                        player.getCooldownTracker().setCooldown(this, 25);
                        ModUtils.performNTimes(6, (i) -> {
                            Vec3d playerLookVec = player.getLookVec();
                            Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 0.7D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 0.7D);
                            Main.proxy.spawnParticle(28, worldIn, playerPos.x + ModRand.getFloat(0.5F), playerPos.y + ModRand.getFloat(0.5F), playerPos.z + ModRand.getFloat(0.5F), 0,0,0);
                        });

                        if(player.canBePushed()) {
                            Vec3d moveVec = player.getLookVec().scale(-((2.0 * 0.2) + 0.1D));
                            player.motionX = moveVec.x;
                            player.motionY = 0.14;
                            player.motionZ = moveVec.z;
                            player.velocityChanged = true;
                        }
                    }
                } else {
                    this.isParrying = false;
                    player.stopActiveHand();
                    this.setPlayerLife = 0;
                    currentLife = 0;
                }
            }
        }
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
