package com.dungeon_additions.da.items.tools;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.init.ModItems;
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
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemExaltedKopis extends ToolSword{

    private String info_loc;
    private boolean isParrying = false;
    private int currentLife = 0;
    private double setPlayerLife = 0;
    private boolean dealtDamage = false;
    private int parryCharge = 0;
    private boolean isDashing = false;
    private int dashTime = 0;

    public ItemExaltedKopis(String name, ToolMaterial material, String info_loc) {
        super(name, material);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
        tooltip.add(TextFormatting.YELLOW + I18n.translateToLocal("description.dungeon_additions.scaled_weapon.name"));
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        attacker.world.playSound((EntityPlayer) null, attacker.posX, attacker.posY, attacker.posZ, SoundsHandler.WARLORD_SWING, SoundCategory.NEUTRAL, 0.6f, 0.5f / (attacker.world.rand.nextFloat() * 0.4F + 0.2f));
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this) && player.hurtTime == 0) {

            if(this.parryCharge > 2) {
                //do dash attack
                Vec3d moveVec = player.getLookVec().scale(3.1F);
                if(player.canBePushed()) {
                    worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.WARLORD_SWING_MAGIC, SoundCategory.NEUTRAL, 1.4f, 0.7f / (worldIn.rand.nextFloat() * 0.4F + 0.2f));
                    player.motionX = moveVec.x;
                    player.motionY = 0.15;
                    player.motionZ = moveVec.z;
                    player.velocityChanged = true;
                    player.setActiveHand(hand);
                    stack.damageItem(5, player);
                    this.isDashing = true;
                    this.parryCharge = 0;
                    player.getCooldownTracker().setCooldown(this, ModConfig.exalted_kopis_cooldown * 20);
                    player.world.playSound(player.posX + 0.5D, player.posY, player.posZ + 0.5D, SoundsHandler.DRAUGR_ELITE_STOMP, SoundCategory.BLOCKS,(float) 1.0F, 1.0F, false);
                }
            } else {
                this.setPlayerLife = 0;
                worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.6f, 0.3f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
                this.currentLife = player.ticksExisted;
                this.dealtDamage = false;
                this.isParrying = true;
                player.hurtResistantTime = 0;
                this.setPlayerLife = player.getHealth();
                player.setActiveHand(hand);
                player.getCooldownTracker().setCooldown(this, 60);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(entityIn instanceof EntityPlayer && !worldIn.isRemote) {
            EntityPlayer player = ((EntityPlayer) entityIn);

            if(player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == this && worldIn.rand.nextInt(4) == 0 && this.parryCharge > 2 && !this.isDashing) {
                Main.proxy.spawnParticle(23, worldIn, player.posX + ModRand.range(-2, 2), player.posY + 0.25 + ModRand.getFloat(1F), player.posZ + ModRand.range(-2, 2), 0,0.05,0, 15128888);
            }

            if(this.isDashing && player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == this) {
                //dash attack
                List<EntityLivingBase> targets = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(2), e -> e != player);
                Main.proxy.spawnParticle(23, worldIn, player.posX, player.posY + 0.25, player.posZ, 0,0.05,0, 15128888);
                Main.proxy.spawnParticle(23, worldIn, player.posX, player.posY + 1.5, player.posZ, 0,0.05,0, 15128888);

                dashTime++;

                if(player.motionX < 0.1 && player.motionZ < 0.1 && dashTime > 5 || dashTime > 35) {
                    this.isDashing = false;
                    player.motionX = 0;
                    player.motionZ = 0;
                    player.motionY = 0;
                    dashTime = 0;
                    player.resetActiveHand();
                }

                for(EntityLivingBase target : targets) {
                    // Vec3d dir = target.getPositionVector().subtract(player.getPositionVector()).normalize();
                    Vec3d moveDir = player.getPositionVector().add(player.getLookVec().add(0, 1.5, 0)).scale(1.5D);

                    if(target.canBeCollidedWith()) {
                        this.onEnemyRammed(player, target, moveDir);
                    }
                }
                //parry mechanic
            } else if (player.getHeldItemMainhand().getItem() == this && isParrying) {
                    boolean didBlockDamage = false;

                    if (player.ticksExisted < currentLife + 11) {
                        if (player.hurtTime > 0) {
                            didBlockDamage = true;
                        }


                        if (didBlockDamage && !this.dealtDamage) {
                            EntityLivingBase attacker = player.getAttackingEntity();
                            assert attacker != null;
                            if(attacker instanceof EntityLivingBase) {
                                attacker.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 1, false, false));
                            }
                            worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.IMPERIAL_SWORD_PARRY, SoundCategory.NEUTRAL, 1.0f, 0.8f / (worldIn.rand.nextFloat() * 0.4F + 0.3f));
                            this.isParrying = false;
                            player.stopActiveHand();
                            this.parryCharge++;
                            player.setHealth((float) this.setPlayerLife);
                            currentLife = 0;
                            player.getCooldownTracker().setCooldown(this, 20);
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

    public void onEnemyRammed(EntityLivingBase user, EntityLivingBase enemy, Vec3d rammingDir) {
        boolean attacked = false;
        float damage = (float) (ModConfig.exalted_kopis_dash_damage * 1.75) + ModUtils.addAbilityBonusDamage(user.getHeldItemMainhand(), 1.5F);

        if(user instanceof EntityPlayer) {
            EntityPlayer actor = ((EntityPlayer) user);
            if(actor.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET) {
                damage = (float) (((ModConfig.exalted_kopis_dash_damage * 1.75) + ModUtils.addAbilityBonusDamage(user.getHeldItemMainhand(), 1.5F)) * ModConfig.incendium_helmet_multipler);
            }
        }

        if(user instanceof EntityPlayer) {
            attacked = enemy.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)user), damage);

        } else {
            attacked = enemy.attackEntityFrom(DamageSource.causeMobDamage(user), damage);
        }

        if(attacked) {
            enemy.knockBack(user, 1.9F, -rammingDir.x, -rammingDir.z);
        }
    }



    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }


    @Override
    protected double getAttackSpeed() {
        return -2.3000000953674316D;
    }

}
