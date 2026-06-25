package com.dungeon_additions.da.event;


import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.config.WorldConfig;
import com.dungeon_additions.da.entity.EntityAbstractBase;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkAssassin;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkSorcerer;
import com.dungeon_additions.da.entity.dark_dungeon.EntityShadowHand;
import com.dungeon_additions.da.entity.desert_dungeon.boss.EntityColossusSigil;
import com.dungeon_additions.da.entity.player.EntityWyrkLazer;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBase;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyTornado;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.init.ModPotions;
import com.dungeon_additions.da.items.armor.VoidiantChestplate;
import com.dungeon_additions.da.items.armor.aegyptia.ItemColossusChestplate;
import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import com.dungeon_additions.da.items.tools.ItemMageStaff;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.items.util.ISweepAttackOverride;
import com.dungeon_additions.da.packets.MessageEmptySwing;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModDamageSource;
import com.dungeon_additions.da.util.handlers.SoundsHandler;
import com.dungeon_additions.da.util.player.PlayerMeleeAttack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber
public class EventWearFlameArmor {

    public Item[] itemsStored;

    public static double cooldownDelegation = 5.0;

    public static final UUID INCENDIUM_HEALTH_MODIFIER = UUID.fromString("0483aa4a-af8d-36a2-8693-22bec9caa265");

    public static final UUID SHIELD_TRINKET_MODIFIER = UUID.fromString("9812aa4a-afc8-33b2-8956-22bec9cab736");
    public static final UUID DIAMOND_SHIELD_TRINKET_MODIFIER = UUID.fromString("7843aa4a-af8d-36a2-1293-69bec9caa675");
    public static final UUID HEART_TRINKET_MODIFIER = UUID.fromString("8724aa4a-af8d-22a2-8693-12bec9caa544");
    public static final UUID SPEED_TRINKET_MODIFIER = UUID.fromString("8321aa4a-af3d-42a2-1983-42bec9caa503");
    private static final NBTTagCompound falter_time = null;

    @SubscribeEvent
    public static void onEquipArmor(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();

        if(base.isPotionActive(ModPotions.HUNTERS_MARK) && base.ticksExisted % 5 == 0) {
            Main.proxy.spawnParticle(27, base.world, base.posX, base.posY + base.getEyeHeight() + 1.5, base.posZ, 0, 0, 0);
        }

        if(base.ticksExisted % 30 == 0 && base.isPotionActive(ModPotions.DEGRADATION)) {
            Main.proxy.spawnParticle(32, base.world,base.posX + ModRand.getFloat(1.5F), base.posY + base.getEyeHeight(), base.posZ + ModRand.getFloat(1.5F), 0, -0.03, 0);
        }

        //faltered potion
        if(base.isPotionActive(ModPotions.FALTERED)) {
            if(Objects.requireNonNull(base.getActivePotionEffect(ModPotions.FALTERED)).getDuration() != 0) {

                if(base.ticksExisted % 8 == 0 && !base.world.isRemote) {
                    Main.proxy.spawnParticle(34, base.world,base.posX + ModRand.getFloat(1F), base.posY + 0.5 + ModRand.getFloat(1), base.posZ + ModRand.getFloat(1F), 0, 0.045, 0);
                }

                if(!base.getEntityData().hasKey("falter_time")) {
                   base.getEntityData().setInteger("falter_time", Objects.requireNonNull(base.getActivePotionEffect(ModPotions.FALTERED)).getDuration());
                } else {
                    int i = base.getEntityData().getInteger("falter_time");
                    int duration = Objects.requireNonNull(base.getActivePotionEffect(ModPotions.FALTERED)).getDuration();
                    //logs the start of the effect
                    if(i - 1 == duration) {
                        Vec3d moveVec = base.getLookVec().scale(-((1.1 * 0.6) + 0.1D));
                        base.getEntityWorld().playSound((EntityPlayer) null, base.posX, base.posY, base.posZ, SoundsHandler.IMPERIAL_SWORD_PARRY, SoundCategory.NEUTRAL, 0.5f, 1F);
                        if(base.canBePushed() && !base.isBeingRidden()) {
                            base.motionX = moveVec.x * 0.7;
                            base.motionY = moveVec.y * 1.4;
                            base.motionZ = moveVec.z * 0.7;
                            base.velocityChanged = true;
                        }
                        ModUtils.circleCallback(2, 10, (pos)-> {
                            pos = new Vec3d(pos.x, 0, pos.y);
                            Vec3d posToo = base.getPositionVector().add(0, 1.4, 0);
                            Vec3d vel = pos.normalize().scale(Math.abs(0.1)).add(ModUtils.yVec(0));
                            Main.proxy.spawnParticle(34, base.getEntityWorld(), posToo.x, posToo.y, posToo.z, vel.x, 0.065, vel.z);
                        });
                    } else {
                        //when the player hits the ground they will be unable to move
                        if(base instanceof EntityPlayer) {
                            base.rotationPitch = 55;
                        }
                        if(base.canBePushed() && base.onGround && !base.isBeingRidden()) {
                            base.motionZ = 0;
                            base.motionX = 0;
                            base.motionY = 0;
                            base.setPosition(base.posX, base.posY, base.posZ);
                            base.velocityChanged = true;
                        }
                    }
                }
            }

        } else if (base.getEntityData().hasKey("falter_time")) {
            base.getEntityData().removeTag("falter_time");
        }

        //do damage from Blood Loss
        if(base.isPotionActive(ModPotions.HEMORRHAGE)) {
            if(base.ticksExisted % 15 == 0) {
                Main.proxy.spawnParticle(33, base.world,base.posX + ModRand.getFloat((float) 1), base.posY + 1.5F + ModRand.getFloat(0.5F), base.posZ + ModRand.getFloat((float) 1), 0, 0, 0);
            }
            if(Objects.requireNonNull(base.getActivePotionEffect(ModPotions.HEMORRHAGE)).getDuration() == 1) {
                base.getEntityWorld().playSound((EntityPlayer) null, base.posX, base.posY, base.posZ, SoundsHandler.HEMORRHAGE_IMPACT, SoundCategory.NEUTRAL, 0.5f, 1F);
                if (!base.world.isRemote) {

                    double level = Objects.requireNonNull(base.getActivePotionEffect(ModPotions.HEMORRHAGE)).getAmplifier();
                    double damage = base.getMaxHealth() * (0.2 + (level >= 3 ? 0.3 : level * 0.1));
                    ModUtils.circleCallback(3, (int) (18 + (level * 5)), (pos)-> {
                        pos = new Vec3d(pos.x, 0, pos.y);
                        Vec3d posToo = base.getPositionVector().add(0, 1.4, 0);
                        Vec3d vel = pos.normalize().scale(Math.abs(ModRand.getFloat(0.25F) + 0.25)).add(ModUtils.yVec(0));
                        Main.proxy.spawnParticle(33, base.getEntityWorld(), posToo.x, posToo.y, posToo.z, vel.x, 0, vel.z);
                    });

                    //do our mobs first
                    if (base instanceof EntityAbstractBase) {
                        EntityAbstractBase boss_base = ((EntityAbstractBase) base);
                        if (boss_base.getHemorrhageResistance() != 1) {
                            float resistantDamage = (float) damage * boss_base.getHemorrhageResistance();
                            if(boss_base.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                                base.heal((float) damage * 0.25F);
                            } else if (damage - resistantDamage >= 20) {
                                base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), (float) 20);
                            } else {
                                base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), (float) damage - resistantDamage);
                            }
                        } else {
                            if (boss_base.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                                boss_base.heal((float) damage * 0.1F);
                            }
                        }

                        //outside mod bosses
                    } else if (!base.isNonBoss()) {

                        if (base.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                            base.heal((float) damage * 0.1F);
                        } else if (damage >= 12) {
                            base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), (float) 12);
                        } else {
                            base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), (float) damage);
                        }
                        //everything else
                    } else {
                        if (base.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                            base.heal((float) damage * 0.1F);
                        } else if (damage >= 20) {
                            base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), 20);
                        } else {
                            base.attackEntityFrom(DamageSource.MAGIC.setDamageBypassesArmor().setMagicDamage(), (float) damage);
                        }
                    }
                }
            }
        }

        if(!base.world.isRemote) {
            //Flame Metal Armor
            if(ModConfig.flame_metal_fire_resistance) {
                if (base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.FLAME_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.FLAME_CHESTPLATE &&
                        base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.FLAME_LEGGINGS && base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.FLAME_BOOTS) {
                    if (base.ticksExisted % 40 == 0) {
                        base.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 0, true, false));
                    }
                }
            }

            //Night Lich Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.POISON)) {
                    base.removePotionEffect(MobEffects.POISON);
                }
            }
            //Draugr Gear
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DRAUGR_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DRAUGR_CHESTPLATE) {
                double healthCurr = base.getHealth() / base.getMaxHealth();
                double bonusAdditive = 2 / healthCurr;
                if(base.hurtTime == 1 && bonusAdditive + base.world.rand.nextInt(ModConfig.champion_armor_chance) >= 11) {
                    base.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 0, false, false));
                }
            }
            //Wyrk Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.WYRK_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.SLOWNESS)) {
                    base.removePotionEffect(MobEffects.SLOWNESS);
                }
            }

            //Trinkets
            if(base instanceof EntityPlayer) {
                EntityPlayer player = ((EntityPlayer) base);
                    ItemStack speedTrinket = ModUtils.findTrinket(new ItemStack(ModItems.SPEED_BOOTS_TRINKET), player);
                    ItemStack strengthTrinket = ModUtils.findTrinket(new ItemStack(ModItems.GLASS_CANNON_TRINKET), player);
                    ItemStack shieldTrinket = ModUtils.findTrinket(new ItemStack(ModItems.SHIELD_TRINKET), player);
                    ItemStack diamondShieldTrinket = ModUtils.findTrinket(new ItemStack(ModItems.DIAMOND_SHIELD_TRINKET), player);
                    ItemStack heartTrinket = ModUtils.findTrinket(new ItemStack(ModItems.HEART_TRINKET), player);
                    ItemStack endermenTrinket = ModUtils.findTrinket(new ItemStack(ModItems.ENDERMEN_TRINKET), player);
                    ItemStack voidTrinket = ModUtils.findTrinket(new ItemStack(ModItems.VOID_TRINKET), player);
                    ItemStack void_hand_trinket = ModUtils.findTrinket(new ItemStack(ModItems.VOID_HAND_TRINKET), player);

                    if(!void_hand_trinket.isEmpty() && !player.getCooldownTracker().hasCooldown(void_hand_trinket.getItem())) {
                        int randI = ModRand.range(1, 101);
                        if(player.isPotionActive(MobEffects.POISON) || player.isPotionActive(MobEffects.WEAKNESS) || player.isPotionActive(MobEffects.SLOWNESS) ||
                                player.isPotionActive(MobEffects.WITHER) || player.isPotionActive(MobEffects.HUNGER)) {

                        if(randI < 60 && randI > 55 && player.ticksExisted % 20 == 0) {
                            List<EntityLivingBase> targets = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(8, 1.5, 8), e -> !e.getIsInvulnerable());
                            if (!targets.isEmpty()) {
                                int maxHandSpawns = 0;
                                for (EntityLivingBase target : targets) {
                                    if (target != player) {
                                        if (target instanceof EntityMob && maxHandSpawns < 5) {
                                            EntityShadowHand hand = new EntityShadowHand(player.world, true);
                                            hand.setPosition(target.posX, target.posY, target.posZ);
                                            player.world.spawnEntity(hand);
                                            maxHandSpawns++;
                                        }
                                        if (target instanceof EntityAbstractBase && maxHandSpawns < 5) {
                                            EntityAbstractBase mod_mob = ((EntityAbstractBase) target);
                                            if (!mod_mob.isFriendlyCreature) {
                                                EntityShadowHand hand = new EntityShadowHand(player.world, true);
                                                hand.setPosition(mod_mob.posX, mod_mob.posY, mod_mob.posZ);
                                                player.world.spawnEntity(hand);
                                                maxHandSpawns++;
                                            }
                                        }
                                    }
                                }
                                if (maxHandSpawns > 0) {
                                    void_hand_trinket.damageItem(1, player);
                                    player.getCooldownTracker().setCooldown(void_hand_trinket.getItem(), 600);
                                }
                            }
                        }
                        }
                    }

                    if(!speedTrinket.isEmpty() && player.getHealth() / player.getMaxHealth() > 0.98) {
                        if(player.ticksExisted % 45 == 0) {
                            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
                        }
                        if(player.hurtTime == 1) {
                            speedTrinket.damageItem(1, player);
                        }
                    }

                    if(!strengthTrinket.isEmpty() && player.getHealth() / player.getMaxHealth() > 0.98) {
                        if(player.ticksExisted % 45 == 0) {
                            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, 0, false, false));
                        }
                        if(player.hurtTime == 1) {
                            strengthTrinket.damageItem(1, player);
                        }
                    }

                    if(!voidTrinket.isEmpty() && player.isPotionActive(MobEffects.WEAKNESS)) {
                        if(base.hurtTime == 1) {
                            voidTrinket.damageItem(1, player);
                        }
                        base.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 80, 0, false, false));
                    }

                    IAttributeInstance attributeInShield = base.getEntityAttribute(SharedMonsterAttributes.ARMOR);
                    IAttributeInstance attributeInDiamond = base.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
                    IAttributeInstance attributeInHeart = base.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                IAttributeInstance attributeInSpeed = base.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                    //shield Trinkets
                    if(!shieldTrinket.isEmpty()) {
                        if(attributeInShield.getModifier(SHIELD_TRINKET_MODIFIER) == null) {
                            attributeInShield.applyModifier(new AttributeModifier(SHIELD_TRINKET_MODIFIER, "shield_trinket_modifier", PotionTrinketConfig.mythic_shield_armor_value, 1).setSaved(false));
                        }
                        if(player.hurtTime == 1) {
                            shieldTrinket.damageItem(1, player);
                        }
                    } else if(attributeInShield.getModifier(SHIELD_TRINKET_MODIFIER) != null) {
                        attributeInShield.removeModifier(SHIELD_TRINKET_MODIFIER);
                    }

                    if(!diamondShieldTrinket.isEmpty()) {
                            if(attributeInDiamond.getModifier(DIAMOND_SHIELD_TRINKET_MODIFIER) == null) {
                                attributeInDiamond.applyModifier(new AttributeModifier(DIAMOND_SHIELD_TRINKET_MODIFIER, "diamond_shield_trinket_modifier", PotionTrinketConfig.exalted_shield_armor_value, 1).setSaved(false));
                            }
                        if(player.hurtTime == 1) {
                            diamondShieldTrinket.damageItem(1, player);
                        }
                    } else if(attributeInDiamond.getModifier(DIAMOND_SHIELD_TRINKET_MODIFIER) != null) {
                        attributeInDiamond.removeModifier(DIAMOND_SHIELD_TRINKET_MODIFIER);
                    }

                    if(!heartTrinket.isEmpty()) {
                        if (attributeInHeart.getModifier(HEART_TRINKET_MODIFIER) == null) {
                            attributeInHeart.applyModifier(new AttributeModifier(HEART_TRINKET_MODIFIER, "heart_trinket_modifier", PotionTrinketConfig.vigorous_journey_health_amount, 1).setSaved(false));
                        }

                        if(player.hurtTime == 1) {
                            heartTrinket.damageItem(1, player);
                        }
                    } else if(attributeInHeart.getModifier(HEART_TRINKET_MODIFIER) != null) {
                        attributeInHeart.removeModifier(HEART_TRINKET_MODIFIER);
                    }

                if(!endermenTrinket.isEmpty()) {
                    if (attributeInSpeed.getModifier(SPEED_TRINKET_MODIFIER) == null) {
                        attributeInSpeed.applyModifier(new AttributeModifier(SPEED_TRINKET_MODIFIER, "endermen_trinket_modifier", PotionTrinketConfig.endermen_trinket_speed_amount, 1).setSaved(false));
                    }

                    if(player.hurtTime == 1) {
                        endermenTrinket.damageItem(1, player);
                    }
                } else if(attributeInSpeed.getModifier(SPEED_TRINKET_MODIFIER) != null) {
                    attributeInSpeed.removeModifier(SPEED_TRINKET_MODIFIER);
                }
            }

            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.WARLORD_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.WITHER)) {
                    base.removePotionEffect(MobEffects.WITHER);
                }
            }

            if(base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.COLOSSUS_CHESTPLATE && base.getHealth() / base.getMaxHealth() <= 0.5) {
                ItemStack chestplate = base.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if(base instanceof EntityPlayer) {
                    EntityPlayer player = ((EntityPlayer) base);
                    if(!player.getCooldownTracker().hasCooldown(chestplate.getItem())) {
                        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 500, 0, false, false));
                        player.getCooldownTracker().setCooldown(chestplate.getItem(), 4800);
                    }
                }
            }

            //Dark Metal Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DARK_METAL_HELMET) {
                if(base.ticksExisted % 40 == 0 && !base.world.isDaytime()) {
                    base.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, false, false));
                }
            }

            //Wyrk Boots
            if(base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.WYRK_BOOTS) {
                if(base.ticksExisted % 40 == 0 && base.world.getBlockState(base.getPosition().down()).getBlock() == Blocks.ICE ||
                        base.ticksExisted % 40 == 0 && base.world.getBlockState(base.getPosition().down()).getBlock() == Blocks.PACKED_ICE ||
                        base.ticksExisted % 40 == 0 && base.world.getBlockState(base.getPosition().down()).getBlock() == Blocks.FROSTED_ICE) {
                    base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
                }
            }

            //Wyrk Boots and Wyrk Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.WYRK_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.WYRK_BOOTS) {
                double healthCurr = base.getHealth() / base.getMaxHealth();
                double bonusAdditive = 2 / healthCurr;
                if(base.hurtTime == 1 && bonusAdditive + base.world.rand.nextInt(ModConfig.wyrk_armor_chance) >= 11) {
                    base.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1, false, false));
                }
            }

            //King's Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.KINGS_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.WITHER) || base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.MINING_FATIGUE)) {
                    base.removePotionEffect(MobEffects.WITHER);
                    base.removePotionEffect(MobEffects.MINING_FATIGUE);
                    base.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 0, false, false));
                }
            }

            //King's Claw
            if(base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.KING_CLAW && base.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == ModItems.KING_CLAW) {
                if(base.ticksExisted % 40 == 0 && base.getHealth()/base.getMaxHealth() <= 0.75) {
                    base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
                }
            }

            //Imperial Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.IMPERIAL_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.WEAKNESS)) {
                    base.removePotionEffect(MobEffects.WEAKNESS);
                }
            }

            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.OBSIDIAN_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.LEVITATION)) {
                    base.removePotionEffect(MobEffects.LEVITATION);
                }

                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.BLINDNESS)) {
                    base.removePotionEffect(MobEffects.BLINDNESS);
                }
            }

            //Incendium Helmet
            if(base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.INCENDIUM_LEGGINGS) {
                if(base instanceof EntityPlayer) {
                    EntityPlayer player = ((EntityPlayer) base);
                    if(player.isActiveItemStackBlocking() && base.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() instanceof ItemShield ||
                            player.isActiveItemStackBlocking() && base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemShield) {
                        base.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 0, false, false));
                    }
                }
            }

            //Imperial Armor
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.IMPERIAL_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.IMPERIAL_CHESTPLATE &&
            base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.IMPERIAL_LEGGINGS && base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.IMPERIAL_BOOTS) {
                double healthCurr = base.getHealth() / base.getMaxHealth();
                double bonusAdditive = 1 / healthCurr;
                if(healthCurr <= 0.2) {
                    bonusAdditive -= 9;
                }


                cooldownDelegation -= 0.07;
                if(base.hurtTime == 1 && bonusAdditive + base.world.rand.nextInt(ModConfig.litic_armor_chance) >= 7 + cooldownDelegation) {
                    if(!base.isPotionActive(MobEffects.ABSORPTION)) {
                        base.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 600, 0, false, false));
                    }
                    EntityLivingBase entityLivingBase = base.getAttackingEntity();
                    if(entityLivingBase != null && base instanceof EntityPlayer) {
                        summonLightningOnPlayerPos((EntityPlayer) base);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void adjustHealthChangeFromArmor(LivingEquipmentChangeEvent event) {
        EntityLivingBase base = event.getEntityLiving();
        IAttributeInstance attributeIn = base.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        //Incendium Armor Health Bonus
        if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.INCENDIUM_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.INCENDIUM_LEGGINGS) {
            if(!base.world.isRemote && attributeIn.getModifier(INCENDIUM_HEALTH_MODIFIER) == null) {
                attributeIn.applyModifier(new AttributeModifier(INCENDIUM_HEALTH_MODIFIER, "incendium_health_modifier", 0.2, 1).setSaved(false));;
            }
        } else {
            if(attributeIn.getModifier(INCENDIUM_HEALTH_MODIFIER) != null) attributeIn.removeModifier(INCENDIUM_HEALTH_MODIFIER);
        }
    }

    @SubscribeEvent
    public static void onPlayerFall(LivingFallEvent event) {
        if(event.getEntityLiving() == null) {
            return;
        }

        if(event.getEntityLiving().isPotionActive(ModPotions.GOLDEN_VOW)) {
            event.setDistance(0);
        }

    }

    @SubscribeEvent
    public static void doShieldUpdates(LivingAttackEvent e) {
        BOMDShieldItem.handleDamageEvent(e);
    }


    private static void summonLightningOnPlayerPos(EntityPlayer actor) {
        EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt2 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt3 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt4 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt5 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt6 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt7 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);
        EntitySkyBolt bolt8 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor, 10);

        Vec3d actorPos = actor.getPositionVector();
        bolt.setPosition(actorPos.x + 2.5, actor.posY, actor.posZ);
        bolt2.setPosition(actorPos.x - 2.5, actor.posY, actor.posZ);
        bolt3.setPosition(actorPos.x, actor.posY, actor.posZ + 2.5);
        bolt4.setPosition(actorPos.x, actor.posY, actor.posZ - 2.5);
        bolt5.setPosition(actorPos.x + 2.25, actor.posY, actor.posZ + 2.25);
        bolt6.setPosition(actorPos.x + 2.25, actor.posY, actor.posZ - 2.25);
        bolt7.setPosition(actorPos.x - 2.25, actor.posY, actor.posZ - 2.25);
        bolt8.setPosition(actorPos.x - 2.25, actor.posY, actor.posZ + 2.25);

        actor.world.spawnEntity(bolt);
        actor.world.spawnEntity(bolt2);
        actor.world.spawnEntity(bolt3);
        actor.world.spawnEntity(bolt4);
        actor.world.spawnEntity(bolt5);
        actor.world.spawnEntity(bolt6);
        actor.world.spawnEntity(bolt7);
        actor.world.spawnEntity(bolt8);
        cooldownDelegation = 25.0;
    }

    @SubscribeEvent
    public static  void onSpawnAssassin(LivingSpawnEvent event)
    {
        //removes assassin spawns that occur when the players don't meet advancement requirements
        EntityLivingBase base = event.getEntityLiving();
        if(base instanceof EntityDarkAssassin) {
            EntityDarkAssassin assassin = ((EntityDarkAssassin) base);
            event.getWorld().playerEntities.forEach((p)-> {
                if(!ModUtils.getAdvancementCompletionAsListBase(p, ModConfig.assassins_spawn_progress) && !assassin.isSpawnOverride()) {
                    assassin.setDead();
                }
            });
        }

        if(base instanceof EntityDarkSorcerer) {
            EntityDarkSorcerer assassin = ((EntityDarkSorcerer) base);
            event.getWorld().playerEntities.forEach((p)-> {
                if(!ModUtils.getAdvancementCompletionAsListBase(p, ModConfig.sorcerers_spawn_progress) && !assassin.isSpawnOverride()) {
                    assassin.setDead();
                }
            });
        }
    }


    @SubscribeEvent(receiveCanceled = true)
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        // Overrides the melee attack of the player if the item used is the sweep attack
        // override interface
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ISweepAttackOverride && event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ToolSword) {
            PlayerMeleeAttack.attackTargetEntityWithCurrentItem(event.getEntityPlayer(), event.getTarget());
            event.setCanceled(true);
        } else {
            event.setCanceled(false);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent()
    public static void onEmptyLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        handleEmptyLeftClick(event);
    }

    /**
     * If the weapon is charged and the player empty left clicks, sends a message to the server to do a sweep attack
     *
     * @param event
     */
    private static void handleEmptyLeftClick(PlayerInteractEvent event) {
        if (event.getItemStack().getItem() instanceof ToolSword) {
            Main.network.sendToServer(new MessageEmptySwing());
        }
    }

}
