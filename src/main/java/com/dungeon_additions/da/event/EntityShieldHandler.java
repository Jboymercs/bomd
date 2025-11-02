package com.dungeon_additions.da.event;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.entity.player.ActionPlayerFallSlam;
import com.dungeon_additions.da.entity.player.ActionPlayerSmallSpearWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.damage.ModIndirectDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EntityShieldHandler {



    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
            ItemStack creepersWillTrinket = ModUtils.findTrinket(ModItems.CREEPER_TRINKET.getDefaultInstance(), player);
            if(!creepersWillTrinket.isEmpty()) {
                creepersWillTrinket.damageItem(1, player);
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.EXPLOSION)
                        .directEntity(player)
                        .stoppedByArmorNotShields().disablesShields().build();

                ModUtils.handleAreaImpact(5, (e) -> (float) 9, player, player.getPositionVector().add(ModUtils.yVec(1)), source);
                player.getEntityWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1F);
                Main.proxy.spawnParticle(19, player.posX, player.posY + 0.25, player.posZ, 0, 0, 0);
                Main.proxy.spawnParticle(19, player.posX, player.posY + 1.25, player.posZ, 0, 0, 0);
                Main.proxy.spawnParticle(19, player.posX, player.posY + 2.25, player.posZ, 0, 0, 0);
            }
        }
    }
    @SubscribeEvent
    public static void afterShieldAndBeforeArmor(LivingHurtEvent event) {
        if(event.getSource() instanceof ModIndirectDamage) {
            ModIndirectDamage damageSource = ((ModIndirectDamage)event.getSource());
            if(damageSource.getStoppedByArmor()) {
                damageSource.isUnblockable = false;
            }

            if(damageSource.getDisablesShields() && event.getEntityLiving() != null && ModUtils.canBlockDamageSource(damageSource, event.getEntityLiving()) && event.getEntityLiving() instanceof EntityPlayer) {
                ((EntityPlayer)event.getEntityLiving()).disableShield(true);
            }
        }

        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
            if(player != null) {
                ItemStack crystalFruitTrinket = ModUtils.findTrinket(ModItems.FROZEN_CRYSTAL_TRINKET.getDefaultInstance(), player);
                ItemStack magicCharmTrinket = ModUtils.findTrinket(ModItems.MAGIC_CHARM_TRINKET.getDefaultInstance(), player);
                ItemStack weaknessTrinket = ModUtils.findTrinket(ModItems.WEAKNESS_TRINKET.getDefaultInstance(), player);
                ItemStack poisonTrinket = ModUtils.findTrinket(ModItems.POISON_TRINKET.getDefaultInstance(), player);
                ItemStack slamTrinket = ModUtils.findTrinket(ModItems.FROZEN_SLAM_TRINKET.getDefaultInstance(), player);
                if(!crystalFruitTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 11);
                    if (randI == 3) {
                        event.setAmount((float) (event.getAmount() * 0.5));
                        crystalFruitTrinket.damageItem(1, player);
                    }
                }

                if(!magicCharmTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 11);
                    if(randI == 5) {
                        //summon friendly spears
                        new ActionPlayerSmallSpearWave().performAction(player);
                        magicCharmTrinket.damageItem(1, player);
                    }
                }

                if(!weaknessTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 21);
                    if(randI == 8) {
                        if(event.getSource().getImmediateSource() != null) {
                            if(event.getSource().getImmediateSource() instanceof EntityLivingBase) {
                                EntityLivingBase entityIn = ((EntityLivingBase) event.getSource().getImmediateSource());
                                entityIn.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 0, false, true));
                                weaknessTrinket.damageItem(1, player);
                            }
                        }
                    }
                }

                if(!poisonTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 11);
                    if(randI == 7) {
                        if(event.getSource().getImmediateSource() != null) {
                            if(event.getSource().getImmediateSource() instanceof EntityLivingBase) {
                                EntityLivingBase entityIn = ((EntityLivingBase) event.getSource().getImmediateSource());
                                entityIn.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0, false, true));
                                poisonTrinket.damageItem(1, player);
                            }
                        }
                    }
                }

                if(!slamTrinket.isEmpty()) {
                    if(event.getSource() == DamageSource.FALL) {
                        double rings = event.getAmount() * 0.5;
                        if(rings > 0.5) {
                            slamTrinket.damageItem(1, player);
                            new ActionPlayerFallSlam((int) rings).performAction(player);
                        }
                    }
                }
            }
        }

        if(event.getSource().getImmediateSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getSource().getImmediateSource());
            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NOVIK_HELMET) {
                if(event.getEntityLiving() != null) {
                    int randI = ModRand.range(1, 11);
                    if(randI == 4) {
                        event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 80, 0, false, true));
                    }
                }
            }

            ItemStack flameTrinket = ModUtils.findTrinket(ModItems.FLAMES_RAGE_TRINKET.getDefaultInstance(), player);
            ItemStack vampireTrinket = ModUtils.findTrinket(ModItems.VAMPIRIC_TRINKET.getDefaultInstance(), player);
            int flameTrinketBonus = 0;
            if(!flameTrinket.isEmpty()) {
                flameTrinketBonus = player.isBurning() ? 2 : 1;
            }

            if(!vampireTrinket.isEmpty()) {
                int randI = ModRand.range(1, 101);
                if(randI < 21 && randI > 14) {
                    player.heal(2);
                    vampireTrinket.damageItem(1, player);
                }
            }

            //Novik Set
            if((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NOVIK_HELMET || player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET) && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.NOVIK_CHESTPLATE &&
                    player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.NOVIK_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.NOVIK_BOOTS) {
                if(event.getEntityLiving() != null) {
                     if(event.getEntityLiving().getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                         float newDamage = event.getAmount();
                         if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                             newDamage = (float) ((event.getAmount() * 1.2) + 1 + flameTrinketBonus);
                         } else {
                            newDamage = (float) ((event.getAmount() * 1.2) + flameTrinketBonus);
                         }
                         if(!flameTrinket.isEmpty()) {
                             flameTrinket.damageItem(1, player);
                         }
                         event.setAmount(newDamage);
                         return;
                     }
                }
            }

            //Apathyr Helmet
            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                if(event.getEntityLiving() != null) {
                    event.setAmount(event.getAmount() + 2 + flameTrinketBonus);
                    if(!flameTrinket.isEmpty()) {
                        flameTrinket.damageItem(1, player);
                    }
                    return;
                }
            }


            //flame Trinket
            if(!flameTrinket.isEmpty()) {
                if(event.getEntityLiving() != null) {
                    event.setAmount(event.getAmount() + flameTrinketBonus);
                    flameTrinket.damageItem(1, player);
                    return;
                }
            }
        }

        float damage = event.getAmount();
        // Factor in elemental armor first

        // Factor in maelstrom armor second


        event.setAmount(damage);
    }
}
