package com.dungeon_additions.da.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

public class PotionDarkdrift extends PotionBase{


    public PotionDarkdrift(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(3,0);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "308E0DFB-20AE-1903-2904-119310E049A2",0, 0);
    }


       @Override
      public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
         return -4 * amplifier;
      }
}
