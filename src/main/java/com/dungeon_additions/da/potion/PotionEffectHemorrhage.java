package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.Main;
import net.minecraft.entity.EntityLivingBase;

public class PotionEffectHemorrhage extends PotionBase{

    public PotionEffectHemorrhage(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(0,2);
    }

}
