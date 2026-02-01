package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.config.PotionTrinketConfig;
import net.minecraft.entity.SharedMonsterAttributes;

public class PotionEffectGoldenDevotion extends PotionBase{

    double health_amount = PotionTrinketConfig.golden_devotion_health_boost;


    public PotionEffectGoldenDevotion(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(0,0);

        this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "179E0DFB-67AE-2904-0392-831010E409A0", health_amount, 1);
    }


}
