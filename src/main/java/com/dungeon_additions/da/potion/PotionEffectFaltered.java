package com.dungeon_additions.da.potion;

import com.dungeon_additions.da.Main;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class PotionEffectFaltered extends PotionBase{


    public PotionEffectFaltered(String name, boolean isBadEffectIn, int liquidColorIn) {
        super(name, isBadEffectIn, liquidColorIn);
        setIconIndex(1,2);
    }

}
