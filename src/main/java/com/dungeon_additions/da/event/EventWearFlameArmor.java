package com.dungeon_additions.da.event;


import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModRand;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventWearFlameArmor {

    public Item[] itemsStored;


    @SubscribeEvent
    public static void onEquipArmor(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();

        if(!base.world.isRemote) {
            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.FLAME_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.FLAME_CHESTPLATE &&
            base.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == ModItems.FLAME_LEGGINGS && base.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.FLAME_BOOTS) {
                if(base.ticksExisted % 40 == 0) {
                    base.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 0, true, false));
                }
            }

            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.NIGHT_LICH_HELMET) {
                if(base.ticksExisted % 40 == 0 && base.isPotionActive(MobEffects.POISON)) {
                    base.removePotionEffect(MobEffects.POISON);
                }
            }

            if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ModItems.DRAUGR_HELMET && base.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ModItems.DRAUGR_CHESTPLATE) {
                double healthCurr = base.getHealth() / base.getMaxHealth();
                double bonusAdditive = 2 / healthCurr;
                if(base.hurtTime == 1 && bonusAdditive + base.world.rand.nextInt(7) >= 1) {
                    base.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200, 0));
                }
            }
        }
    }
}
