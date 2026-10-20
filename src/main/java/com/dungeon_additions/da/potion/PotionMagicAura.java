package com.dungeon_additions.da.potion;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionMagicAura extends PotionBase{

    public PotionMagicAura(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(3,1);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "236E0DFB-03AE-3814-7432-304238E030A2",0.1, 1);
    }
}
