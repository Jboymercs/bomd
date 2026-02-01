package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.Main;
import net.minecraft.entity.EntityLivingBase;

public class PotionEffectMarked extends PotionBase {

    public PotionEffectMarked(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(2,0);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        if(entity.ticksExisted % 5 == 0) {
            Main.proxy.spawnParticle(26, entity.posX, entity.posY + entity.getEyeHeight() + 1, entity.posZ, 0, 0, 0);
        }
    }

}
