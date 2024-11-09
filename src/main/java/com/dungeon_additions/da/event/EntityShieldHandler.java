package com.dungeon_additions.da.event;

import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModReference;
import com.dungeon_additions.da.util.ModUtils;
import com.dungeon_additions.da.util.damage.ModIndirectDamage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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

        float damage = event.getAmount();
        // Factor in elemental armor first

        // Factor in maelstrom armor second


        event.setAmount(damage);
    }
}
