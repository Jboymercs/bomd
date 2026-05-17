package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.Main;
import com.dungeon_additions.da.util.ModRand;
import net.minecraft.entity.EntityLivingBase;

public class PotionEffectDegradation extends PotionBase{


    public PotionEffectDegradation(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(2,1);
    }
}
