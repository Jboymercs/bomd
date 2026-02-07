package com.dungeon_additions.da.potion;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionEffectFieryRespite extends PotionBase {


    public PotionEffectFieryRespite(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setBeneficial();
        setIconIndex(1,1);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "096E0DFB-04AE-4095-0712-541010E093A7",0.3F, 1);
    }
}
