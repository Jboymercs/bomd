package com.dungeon_additions.da.potion;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionPoisonGarnish extends PotionBase{


    public PotionPoisonGarnish(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
            setIconIndex(0,1);
            setBeneficial();
    }
}
