package com.dungeon_additions.da.event;


import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkAssassin;
import com.dungeon_additions.da.entity.dark_dungeon.EntityDarkSorcerer;
import com.dungeon_additions.da.entity.sky_dungeon.EntitySkyBolt;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.items.shield.BOMDShieldItem;
import com.dungeon_additions.da.items.tools.ToolSword;
import com.dungeon_additions.da.items.util.ISweepAttackOverride;
import com.dungeon_additions.da.packets.MessageEmptySwing;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.player.PlayerMeleeAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@Mod.EventBusSubscriber
public class EventWearFlameArmor {

    public Item[] itemsStored;

    public static double cooldownDelegation = 5.0;

    public static final UUID INCENDIUM_HEALTH_MODIFIER = UUID.fromString("0483aa4a-af8d-36a2-8693-22bec9caa265");

    @SubscribeEvent
    public static void onEquipArmor(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();

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
                attributeIn.applyModifier(new AttributeModifier(INCENDIUM_HEALTH_MODIFIER, "soul_speed_modifier", 0.2, 1).setSaved(false));;
            }
        } else {
            if(attributeIn.getModifier(INCENDIUM_HEALTH_MODIFIER) != null) attributeIn.removeModifier(INCENDIUM_HEALTH_MODIFIER);
        }
    }

    @SubscribeEvent
    public static void doShieldUpdates(LivingAttackEvent e) {
        BOMDShieldItem.handleDamageEvent(e);
    }


    private static void summonLightningOnPlayerPos(EntityPlayer actor) {
        EntitySkyBolt bolt = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt2 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt3 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt4 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt5 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt6 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt7 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);
        EntitySkyBolt bolt8 = new EntitySkyBolt(actor.world, actor.getPositionVector().add(0, 10, 0), actor);

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
                if(!ModUtils.getAdvancementCompletionAsList(p, ModConfig.assassins_spawn_progress) && !p.isCreative()) {
                    assassin.setDead();
                }
            });
        }

        if(base instanceof EntityDarkSorcerer) {
            EntityDarkSorcerer assassin = ((EntityDarkSorcerer) base);
            event.getWorld().playerEntities.forEach((p)-> {
                if(!ModUtils.getAdvancementCompletionAsList(p, ModConfig.sorcerers_spawn_progress) && !p.isCreative()) {
                    assassin.setDead();
                }
            });
        }
    }


    @SubscribeEvent(receiveCanceled = true)
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        // Overrides the melee attack of the player if the item used is the sweep attack
        // override interface
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ISweepAttackOverride) {
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
