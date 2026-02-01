package com.dungeon_additions.da.event;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.entity.player.ActionPlayerFallSlam;
import com.dungeon_additions.da.entity.player.ActionPlayerSmallSpearWave;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.damage.ModIndirectDamage;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.dungeon_additions.da.util.player.PlayerMeleeAttack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EntityShieldHandler {



    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
            ItemStack creepersWillTrinket = ModUtils.findTrinket(new ItemStack(ModItems.CREEPER_TRINKET), player);
            ItemStack confettiTrinket = ModUtils.findTrinket(new ItemStack(ModItems.CONFETTI_TRINKET), player);
            if(!player.world.isRemote) {
                if (!creepersWillTrinket.isEmpty()) {
                    creepersWillTrinket.damageItem(1, player);
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.EXPLOSION)
                            .directEntity(player)
                            .stoppedByArmorNotShields().disablesShields().build();

                    ModUtils.handleAreaImpact(5, (e) -> (float) 9, player, player.getPositionVector().add(ModUtils.yVec(1)), source);
                    player.getEntityWorld().playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1F);
                    Main.proxy.spawnParticle(19, player.world, player.posX, player.posY + 0.25, player.posZ, 0, 0, 0);
                    Main.proxy.spawnParticle(19, player.world,  player.posX, player.posY + 1.25, player.posZ, 0, 0, 0);
                    Main.proxy.spawnParticle(19, player.world, player.posX, player.posY + 2.25, player.posZ, 0, 0, 0);
                }

                if(!confettiTrinket.isEmpty()) {

                        ModUtils.performNTimes(12, (i) -> {
                            Main.proxy.spawnParticle(23, player.world, player.posX + ModRand.range(-1, 1) + ModRand.getFloat(1), player.posY + ModRand.range(0, 3) + ModRand.getFloat(1), player.posZ + ModRand.range(-1, 1) + ModRand.getFloat(1), 0, -0.03, 0, 3145519);
                        });
                        ModUtils.performNTimes(12, (i) -> {
                            Main.proxy.spawnParticle(23, player.world, player.posX + ModRand.range(-1, 1) + ModRand.getFloat(1), player.posY + ModRand.range(0, 3) + ModRand.getFloat(1), player.posZ + ModRand.range(-1, 1) + ModRand.getFloat(1), 0, -0.03, 0, 3099391);
                        });
                        ModUtils.performNTimes(12, (i) -> {
                            Main.proxy.spawnParticle(23,player.world, player.posX + ModRand.range(-1, 1) + ModRand.getFloat(1), player.posY + ModRand.range(0, 3) + ModRand.getFloat(1), player.posZ + ModRand.range(-1, 1) + ModRand.getFloat(1), 0, -0.03, 0, 16450048);
                        });
                        ModUtils.performNTimes(12, (i) -> {
                            Main.proxy.spawnParticle(23, player.world, player.posX + ModRand.range(-1, 1) + ModRand.getFloat(1), player.posY + ModRand.range(0, 3) + ModRand.getFloat(1), player.posZ + ModRand.range(-1, 1) + ModRand.getFloat(1), 0, -0.03, 0, 16450255);
                        });
                        player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.CONFETTI_SOUND, SoundCategory.NEUTRAL, 1f, 1f);

                    List<EntityPlayer> targets = player.world.getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().grow(10, 1.5, 10), e -> !e.getIsInvulnerable());

                    if(!targets.isEmpty()) {
                        for(EntityPlayer playerToo : targets) {
                            playerToo.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 1, false, true));

                        }
                        confettiTrinket.damageItem(1, player);
                    }
                }
            }

            //adventuric set

                //adventuric set
                double deathChance = 0;
                ItemStack adventureHelmet = ModUtils.findArmorPiece(new ItemStack(ModItems.ADVENTUIC_HELMET), player, EntityEquipmentSlot.HEAD);
                ItemStack adventureChestplate = ModUtils.findArmorPiece(new ItemStack(ModItems.ADVENTUIC_CHESTPLATE), player, EntityEquipmentSlot.CHEST);
                ItemStack adventureLeggings = ModUtils.findArmorPiece(new ItemStack(ModItems.ADVENTUIC_LEGGINGS), player, EntityEquipmentSlot.LEGS);
                ItemStack adventureBoots = ModUtils.findArmorPiece(new ItemStack(ModItems.ADVENTUIC_BOOTS), player, EntityEquipmentSlot.FEET);


                if (!adventureHelmet.isEmpty()) {
                    deathChance += 0.1;
                }
                if (!adventureChestplate.isEmpty()) {
                    deathChance += 0.1;
                }
                if (!adventureLeggings.isEmpty()) {
                    deathChance += 0.1;
                }
                if (!adventureBoots.isEmpty()) {
                    deathChance += 0.1;
                }

                //do death ability
                if (deathChance > 0.09 && !player.getCooldownTracker().hasCooldown(adventureHelmet.getItem()) && !player.getCooldownTracker().hasCooldown(adventureChestplate.getItem()) &
                        !player.getCooldownTracker().hasCooldown(adventureLeggings.getItem()) & !player.getCooldownTracker().hasCooldown(adventureBoots.getItem())) {
                    player.isDead = false;
                    player.deathTime = 0;
                    player.hurtResistantTime = 30;

                    player.setHealth((float) (player.getMaxHealth() * deathChance));
                    player.world.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundsHandler.DEATHBLOW_SOUND, SoundCategory.NEUTRAL, 1.1f, 1f);
                    ModUtils.circleCallback(2, 9, (pos) -> {
                        pos = new Vec3d(pos.x, 0, pos.y);
                        Main.proxy.spawnParticle(13, player.posX + pos.x, player.posY + 1, player.posZ + pos.z, 0, 0.05, 0);
                    });

                    Vec3d moveVec = player.getLookVec().scale(-((0.9 * 0.6) + 0.1D));
                    if(player.canBePushed()) {
                        player.motionX = moveVec.x;
                        player.motionY = moveVec.y * 0.8;
                        player.fallDistance = 0;
                        player.motionZ = moveVec.z;
                        player.velocityChanged = true;
                    }
                    if (!adventureHelmet.isEmpty()) {
                        player.getCooldownTracker().setCooldown(adventureHelmet.getItem(), 30 * 30 * 20);
                    }
                    if (!adventureChestplate.isEmpty()) {
                        player.getCooldownTracker().setCooldown(adventureChestplate.getItem(), 30 * 30 * 20);
                    }
                    if (!adventureLeggings.isEmpty()) {
                        player.getCooldownTracker().setCooldown(adventureLeggings.getItem(), 30 * 30 * 20);
                    }
                    if (!adventureBoots.isEmpty()) {
                        player.getCooldownTracker().setCooldown(adventureBoots.getItem(), 30 * 30 * 20);
                    }
                    event.setCanceled(true);
                }
        }
    }
    @SubscribeEvent
    public static void afterShieldAndBeforeArmor(LivingHurtEvent event) {

        //shields
        if(event.getSource() instanceof ModIndirectDamage) {
            ModIndirectDamage damageSource = ((ModIndirectDamage)event.getSource());
            if(damageSource.getStoppedByArmor()) {
                damageSource.isUnblockable = false;
            }

            if(damageSource.getDisablesShields() && event.getEntityLiving() != null && ModUtils.canBlockDamageSource(damageSource, event.getEntityLiving()) && event.getEntityLiving() instanceof EntityPlayer) {
                ((EntityPlayer)event.getEntityLiving()).disableShield(true);
            }
        }

        final float originalDamage = event.getAmount();
        float totalDamage = event.getAmount();

        //damage calculations start
        if(event.getEntityLiving() instanceof EntityPlayer) {
            //damage calculation

            EntityPlayer player = ((EntityPlayer) event.getEntityLiving());
            if(player != null) {

                if(event.getSource() instanceof ModIndirectDamage) {

                }

                //reduces damage based on source
                //mage set
                if(event.getSource() instanceof ModIndirectDamage && Objects.equals(((ModIndirectDamage) event.getSource()).damageType, ModDamageSource.MAGIC) || event.getSource() == DamageSource.MAGIC) {
                    double magicDamageReduction = 0;
                    if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.MAGE_HELMET || player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
                        magicDamageReduction += 0.1;
                    } else if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.WYRK_HELMET) {
                        magicDamageReduction += 0.05;
                    }
                    if(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.MAGE_CHESTPLATE) {
                        magicDamageReduction += 0.1;
                    }
                    if(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.MAGE_LEGGINGS) {
                        magicDamageReduction += 0.1;
                    }
                    if(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.MAGE_BOOTS) {
                        magicDamageReduction += 0.1;
                    } else if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.WYRK_BOOTS) {
                        magicDamageReduction += 0.05;
                    }
                    //reduces the damage if its magic based on how many pieces of the mages set the player is wearing
                  //  event.setAmount((float) (totalDamage - (originalDamage * magicDamageReduction)));
                    totalDamage -= (float) (originalDamage * magicDamageReduction);
                }

                //golden devotion buff
                if(!(event.getSource() instanceof ModIndirectDamage && Objects.equals(((ModIndirectDamage) event.getSource()).damageType, ModDamageSource.MAGIC)) && !(event.getSource() == DamageSource.MAGIC)) {
                    if(player.isPotionActive(ModPotions.GOLDEN_DEVOTION)) {
                     //   event.setAmount((float) (totalDamage - (originalDamage * PotionTrinketConfig.golden_devotion_reduction_amount)));
                        totalDamage -= (float) (originalDamage * PotionTrinketConfig.golden_devotion_reduction_amount);
                    }
                }

                ItemStack crystalFruitTrinket = ModUtils.findTrinket(new ItemStack(ModItems.FROZEN_CRYSTAL_TRINKET), player);
                ItemStack magicCharmTrinket = ModUtils.findTrinket(new ItemStack(ModItems.MAGIC_CHARM_TRINKET), player);
                ItemStack weaknessTrinket = ModUtils.findTrinket(new ItemStack(ModItems.WEAKNESS_TRINKET), player);
                ItemStack poisonTrinket = ModUtils.findTrinket(new ItemStack(ModItems.POISON_TRINKET), player);
                ItemStack slamTrinket = ModUtils.findTrinket(new ItemStack(ModItems.FROZEN_SLAM_TRINKET), player);
                ItemStack goldenMarkTrinket = ModUtils.findTrinket(new ItemStack(ModItems.GOLDEN_MARK_TRINKET), player);


                //reduces damage by chance
                if(!crystalFruitTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 11);
                    if (randI == 3) {
                      //  event.setAmount((float)(totalDamage - (originalDamage * 0.5)));
                        totalDamage -= (float) (originalDamage * 0.5);
                        crystalFruitTrinket.damageItem(1, player);
                    }
                }

                //golden mark trinket
                if(!goldenMarkTrinket.isEmpty()) {
                    int randI = ModRand.range(1, 16);
                    if(randI == 7) {
                        if(event.getSource().getImmediateSource() instanceof EntityLivingBase) {
                            EntityLivingBase base = ((EntityLivingBase) event.getSource().getImmediateSource());
                            if(base != null) {
                                base.addPotionEffect(new PotionEffect(ModPotions.HUNTERS_MARK, 600, 0, false, false));
                                goldenMarkTrinket.damageItem(1, player);
                            }
                        }
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

        if(event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer) event.getSource().getTrueSource());
            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NOVIK_HELMET) {
                if(event.getEntityLiving() != null) {
                    int randI = ModRand.range(1, 11);
                    if(randI == 4) {
                        event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 80, 0, false, true));
                    }
                }
            }

            ItemStack flameTrinket = ModUtils.findTrinket(new ItemStack(ModItems.FLAMES_RAGE_TRINKET), player);
            ItemStack vampireTrinket = ModUtils.findTrinket(new ItemStack(ModItems.VAMPIRIC_TRINKET), player);
            if(!flameTrinket.isEmpty()) {
                int flameTrinketBonus = player.isBurning() ? 4 : 2;
              //  event.setAmount(flameTrinketBonus + totalDamage);
                totalDamage += flameTrinketBonus;
                flameTrinket.damageItem(1, player);
            }

            if(!vampireTrinket.isEmpty()) {
                int randI = ModRand.range(1, 101);
                if(randI < 21 && randI > 14) {
                    player.heal(2);
                    vampireTrinket.damageItem(1, player);
                }
            }

            //Novik Set
            if(((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NOVIK_HELMET || player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET)) && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.NOVIK_CHESTPLATE &&
                    player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.NOVIK_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.NOVIK_BOOTS) {
                if(event.getEntityLiving() != null) {
                     if(event.getEntityLiving().getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                         float newDamage = event.getAmount();
                         if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                             newDamage = (float) ((originalDamage * 0.2) + 2);
                         } else {
                            newDamage = (float) ((originalDamage * 0.2));
                         }
                         totalDamage += newDamage;
                        // event.setAmount(totalDamage + newDamage);
                     }
                }
                //Apathyr Helmet only
            } else if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                if(event.getEntityLiving() != null) {
                 //   event.setAmount(totalDamage + 2);
                    totalDamage += 2;
                }
            }

        }

        //universal effects
        if(event.getEntityLiving() != null) {
            EntityLivingBase base = ((EntityLivingBase) event.getEntityLiving());
            if(base.isPotionActive(ModPotions.HUNTERS_MARK)) {
                //  event.setAmount((float) (totalDamage + (originalDamage * PotionTrinketConfig.hunters_mark_damage_increase)));
                totalDamage += (float) (totalDamage * PotionTrinketConfig.hunters_mark_damage_increase);
            }
        }


        event.setAmount(totalDamage);

    }
}
