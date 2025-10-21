package com.dungeon_additions.da.event;

import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModIndirectDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EntityShieldHandler {



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
            //Novik Set
            if((player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NOVIK_HELMET || player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET) && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.NOVIK_CHESTPLATE &&
                    player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.NOVIK_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.NOVIK_BOOTS) {
                if(event.getEntityLiving() != null) {
                     if(event.getEntityLiving().getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                         float newDamage;
                         if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                             newDamage = (float) ((event.getAmount() * 1.2) + 1);
                         } else {
                            newDamage = (float) (event.getAmount() * 1.2);
                         }
                         event.setAmount(newDamage);
                         return;
                     }
                }
            }

            //Apathyr Helmet
            if(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.APATHYR_HELMET && player.getHealth() / player.getMaxHealth() <= 0.5) {
                if(event.getEntityLiving() != null) {
                    event.setAmount(event.getAmount() + 2);
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
