package com.dungeon_additions.da.potion;


import com.dungeon_additions.da.config.PotionTrinketConfig;
import com.dungeon_additions.da.init.ModPotions;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PotionEffectClairvoyance extends PotionBase{


    public PotionEffectClairvoyance(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(2,2);
    }

    @SubscribeEvent
    public static void onExperiencingDeath(LivingExperienceDropEvent event)
    {
        if(event.getAttackingPlayer() != null) {
            if(event.getAttackingPlayer().isPotionActive(ModPotions.CLAIRVOYANCE) && event.getDroppedExperience() > 0) {
                float multiplier =  (float) PotionTrinketConfig.clairvoyance_multiplier;
                event.setDroppedExperience((int) (event.getDroppedExperience() * multiplier));
            }
        }
    }
}
